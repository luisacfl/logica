/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

    //CODIGO ANTIGUO PARA ÁRBOL FINAL, NO SE QUE TAN ÚTIL ES AHORA, lo dejamos por si acaso
    /*public static void arbolFinal(){
       stack.clear();
       for(int i=polaca.length(); i>=0; i--){
           if(operadores.contains(String.valueOf(polaca.charAt(i)))){
               if(polaca.charAt(i)=='&'){
                   stack.push('.');
                   stack.push('.');
                   if(polaca.charAt(i-1)=='!'){
                       Node leaf = new  Node(polaca.charAt(i-2));
                       leaf.setneg(false);
                       stack.pop();
                   }
                   else if(atomos.contains(String.valueOf(polaca.charAt(i-1)))){
                      Node leaf = new  Node(polaca.charAt(i-2));
                      stack.pop();
                   }
                   else if(operadores.contains(String.valueOf(polaca.charAt(i-1)))){
                       if()
                   }
               }
           }
       sd
    }*/

package logica;

/**
 *
 * @author luisa
 */



import java.util.*;
import java.math.*;
import java.util.ArrayList;

public class Logica {
    //Nodos complejos (guardan strings)
    private static class NodeC{
        NodeC left; //Une los y's
        NodeC right; //Solo se abre si existe un "v" (|)
        String cadena; //Cadena con formulas individuales
        boolean cerrada; //Rama cerrada o no
        
        public NodeC(){
            left=null;
            right=null;
            cadena="";

            cerrada=false;
        }
        public NodeC(String s){
            left=null;
            right=null;
            cadena=s;
            cerrada=false;
        }
        public boolean esHoja(){
            if(left==null && right==null){
                return true;
            }
            return false;
        }
         public void setleft(NodeC l){
            left=l;
        }
        public void setright(NodeC r){
            right=r;
        }
        public void setvalue(String s){
            cadena=s;
        }
        public void setclose(boolean n){
            cerrada=n;
        }
        public NodeC getleft(){
            return left;
        }
        public NodeC getright(){
            return right;
        }
        public String getstring(){
            return cadena;
        }
        public boolean getisclosed(){
            return cerrada;
        }
    }
    //Nodos simples para checar si esta bien formada
    private static class Node{
        Node left;
        Node right;
        char value;
        boolean num;
        
        public Node(){
            left=null;
            right=null;
            value=' ';
            
        }
        public Node(char x){
            value=x;
        }
        public boolean esHoja(){
            if(left==null && right==null){
                return true;
            }
            return false;
        }
        public void setleft(Node l){
            left=l;
        }
        public void setright(Node r){
            right=r;
        }
        public void setvalue(char v){
            value=v;
        }
        public void setnum(boolean n){
            num=n;
        }
        public Node getleft(){
            return left;
        }
        public Node getright(){
            return right;
        }
        public char getvalue(){
            return value;
        }
        public boolean getnum(){
            return num;
        }
    }
    //Esto se utilizó en un comienzo para asignar valores (tablas de verdad)
    private static class Bin{
        ArrayList arrC;
        ArrayList arrB;
        public Bin(){
            arrC=new ArrayList() ;
            arrB=new ArrayList();
        }
    }
    
    static String nueva="";
    static Bin b;
    static String atomos="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static String operadores="!|&>=:";
    static String parentesisA="({[";
    static String parentesisC=")}]";
    static Stack stack=new Stack();
    static String expresion="";
    static String polaca="";
    static Node cabeza;
    static NodeC head; //cabeza del arbol final
    static NodeC actual;
    static NodeC encontrado;
    static boolean valor=true;
    static boolean once=true;
    static boolean array[];
    static boolean let[]=new boolean[56];
  
    
    //DONE: Checar conclusión (SEGÚN YO YA LO HACE JUNTO CON LA FORMULA PUES ":" LO TOMA COMO OPERADOR
    //DONE: Negada, replace (:) por (&)
    public static boolean validezConc(String exp){
        StringBuilder temporal=new StringBuilder(exp);
        if(temporal.indexOf(":")>0){
            if(temporal.charAt(temporal.indexOf(":")-1)=='X'){
                temporal.replace(temporal.indexOf(":")-1, temporal.indexOf(":")+1, "(!(");
            }
            else{
                temporal.replace(temporal.indexOf(":"), temporal.indexOf(":")+1, "&(!(");
            }
            temporal.append("))");
            expresion=temporal.toString();
            if(temporal.indexOf(":")>0){
                System.out.println("La formula no está bien formada. Contiene dos conclusiones.");
                return false;
            }
            else{
                return true;
            }
        }
        else{
           System.out.println("La formula esta bien formada, pero no podemos saber si es valida, pues no hay conclusión."); 
           return false;
        }
    }

//(?)TODO: Desglose String ESTO AUN NO SÉ QUE TAN NECESARIO ES (Más efectivo, pero no tan necesario???)
//Árboles
// Crear el árbol con fórmulas separadas por comas
    public static void impArbol(NodeC nodo){
	if (nodo == null) return;
        System.out.println(nodo.cadena);
	impArbol(nodo.left);
	impArbol(nodo.right);
    }
    public static void agregaY(NodeC nodo, String a, String b){
        if(nodo.esHoja()){
            NodeC leaf= new NodeC(a);
            NodeC leaf2= new NodeC(b);
            nodo.setleft(leaf);
            nodo.left.setleft(leaf2);
            return;
        }
        agregaY(nodo.left, a, b);
        if(nodo.right!=null){
            agregaY(nodo.right, a, b);
        }
    }
    public static void agregaO(NodeC nodo, String a, String b){
        if(nodo.esHoja()){
            NodeC leaf= new NodeC(a);
            NodeC leaf2= new NodeC(b);
            nodo.setleft(leaf);
            nodo.setright(leaf2);
            return;
        }
        agregaO(nodo.left, a, b);
        if(nodo.right!=null)
            agregaO(nodo.right, a, b);
    }
    public static void arbolComas(String f){
        String aux; //Para crear las hojas
        for(int i=0; i<f.length(); i++){
            if(f.charAt(i)==','){
                aux=f.substring(0, i);
                f=f.substring(i+1);
                aux=inutiles(aux);
                //aux=comas(aux);
                if(head==null){
                    head=new NodeC(aux);
                    actual=head;
                }
                else{
                    NodeC leaf = new NodeC(aux);
                    actual.setleft(leaf);
                    actual=leaf;
                }
            }
            if(f.indexOf(',')<0){
                f=inutiles(f);
                //f=comas(f);
                if(head==null){
                    head=new NodeC(f);
                    actual=head;
                    
                }
                else{
                    NodeC leaf = new NodeC(f);
                    actual.setleft(leaf);
                    actual=leaf;
                }
            }
        }
    }
    public static void cierraAbajo(NodeC nodo){
        if(nodo==null)return;
        nodo.setclose(true);
        cierraAbajo(nodo.left);
        cierraAbajo(nodo.right);
    }
    public static void busqueda(String atomo, NodeC nodo){
        encontrado=new NodeC(null);
        String busca;
        if(nodo==null) return;
        if(atomo.length()==1){
            busca="!"+atomo;
        }
        else{
            busca=atomo.substring(1);
        }
        if(nodo.cadena.equals(busca)){
            encontrado=nodo;
            cierraAbajo(encontrado);
            return;
        }
        busqueda(atomo, nodo.left);        
        if(nodo.right!=null){
            busqueda(atomo, nodo.right);
        }
    }
    public static void validacion(NodeC nodo){
        if(nodo==null)return;
        if(nodo.esHoja() && !nodo.cerrada){
            valor=false;
            return;
        }
        validacion(nodo.left);
        if(nodo.right!=null){
            validacion(nodo.right);
        }
    }
    public static void ramas(NodeC nodo){
        String atomo;
        NodeC cierre= new NodeC();
        if(nodo.esHoja()){
            //No estoy segura si se pueda hacer esto desde ahorita, mejor lo hago después
            /*if(!nodo.getisclosed()){
                System.out.println("No válido.");
            }*/
            return;
        }   
        if(!nodo.getisclosed()){
            if(nodo.cadena.length()<=2){
                busqueda(nodo.cadena, nodo);
                //Esto ya lo hace dentro de busqueda
                /*if(encontrado!=null){
                    cierraAbajo(encontrado);
                    encontrado=null;
                }*/
            }
            ramas(nodo.left);
            if(nodo.right!=null){
                ramas(nodo.right);
            }
        }
    }
    public static void desgloseArbol(NodeC nodo){
        int indice=0;
        int parentesis=0;
        String a, b;
        if(nodo==null)return;
        if(nodo.cadena.length()>2){
            for(int i=0; i<nodo.cadena.length(); i++){
                if(parentesisA.contains(String.valueOf(nodo.cadena.charAt(i)))){
                    parentesis++;
                }
                else if(parentesisC.contains(String.valueOf(nodo.cadena.charAt(i)))){
                    parentesis--;
                }
                else if(operadores.contains(String.valueOf(nodo.cadena.charAt(i)))&&nodo.cadena.charAt(i)!='!'&&parentesis==0){
                    indice=i; 
                    //break;
                }
            }
            a=nodo.cadena.substring(0, indice);
            b=nodo.cadena.substring(indice+1);
            a=inutiles(a);
            b=inutiles(b);
            if(nodo.cadena.charAt(indice)=='&'){
                agregaY(nodo, a, b);
            }
            else if(nodo.cadena.charAt(indice)=='|'){
                agregaO(nodo, a, b);
            }
        }
        desgloseArbol(nodo.left);
        desgloseArbol(nodo.right);
    }
// Desglosar fórmulas en árbol creado
// Checar si hoja tiene string con 1 o 2 de length (átomo)
// Mandar a llamar ahí (en esa hoja) otra función que busque ese mismo átomo negado o no y cerrar
// Si lo encuentra, mandar a llamar ahí otra función para que inserte el cerrado en el último hijo de las ramas de abajo 
            
    public static boolean par(String e){
        for(int i=0; i<e.length(); i++){
            if(parentesisA.contains(String.valueOf(e.charAt(i)))){
                stack.push(e.charAt(i));
            }
            else if(parentesisC.contains(String.valueOf(e.charAt(i)))){
                if(!stack.isEmpty()){
                    stack.pop();
                }
                else {
                    System.out.println("No es una fórmula bien formadaPar1");
                    return false;   
                }
            }
        }
        if(!stack.isEmpty()){
            System.out.println("No es una fórmula bien formada 1");
            return false;
        }
        return true;
    }
    public static String cambiaImpl(String e){
        int inserta=0;
        StringBuilder temporal = new StringBuilder(e);
        for(int i=0; i<temporal.length(); i++){
                if(temporal.charAt(i)=='>'){
                    temporal.replace(i, i+1, "|");
                    if(atomos.contains(String.valueOf(temporal.charAt(i-1)))){
                        temporal.insert(i-1, '!');
                    }
                    else if(parentesisC.contains(String.valueOf(temporal.charAt(i-1)))){
                        stack.push(temporal.charAt(i-1));
                        for(int j=i-2; !stack.isEmpty(); j--){
                            if(parentesisC.contains(String.valueOf(temporal.charAt(j)))){
                                stack.push(temporal.charAt(j));
                            }
                            else if(parentesisA.contains(String.valueOf(temporal.charAt(j)))){
                                stack.pop();
                            }
                            if(stack.isEmpty()){
                                inserta=j;
                                break;
                            }
                        }
                        temporal.insert(inserta, '!');
                    }
                }
            
        }
        //System.out.println(temporal);
       return temporal.toString(); 
        
    }
    public static String comas(String s){
        StringBuilder temporal = new StringBuilder(s);
        int i=0, parentesis=0;
        char c=0;
        while(temporal.length()>i){	// Ciclo para cambiar ^ por , en la fÛrmula principal
		c=temporal.charAt(i);
		if(parentesisA.contains(String.valueOf(temporal.charAt(i)))) parentesis++;
		else if(parentesisC.contains(String.valueOf(temporal.charAt(i)))) parentesis --;
		else if(parentesis == 0 && c=='&') temporal.replace(i, i+1, ",");	
		i++;
	}
        return temporal.toString();
    }
    public static String cambiaDobImpl(String e){
        String tempP="";
        String tempQ="";
        String total="";
        int preAtomo=0, neg=0;
        boolean entra=false;
        int pre=0, pos=e.length();
            StringBuilder temporal = new StringBuilder(e);
            for(int i=0; i<temporal.length(); i++){
                    //Establece tempP que es lo que esta antes del =
                    if(temporal.charAt(i)=='='){
                        //Si es un atomo checa si esta negado, incluye todos los !
                        if(atomos.contains(String.valueOf(temporal.charAt(i-1)))){
                            preAtomo=i-1;
                            while(temporal.charAt(preAtomo)!='('){
                                preAtomo--;
                                entra=true;
                            }
                            if(entra){
                                preAtomo++;
                                entra=false;
                            }
                            tempP=temporal.substring(preAtomo, i);
                            pre=preAtomo;
                        }
                        //si es parentesis busca el último parentesis 
                        else if(parentesisC.contains(String.valueOf(temporal.charAt(i-1)))){
                            stack.push(temporal.charAt(i-1));
                            for(int j=i-2; !stack.isEmpty(); j--){
                                if(parentesisC.contains(String.valueOf(temporal.charAt(j)))){
                                    stack.push(temporal.charAt(j));
                                }
                                else if(parentesisA.contains(String.valueOf(temporal.charAt(j)))){
                                    stack.pop();
                                }
                                if(stack.isEmpty()){
                                    preAtomo=j-1;
                                    while(temporal.charAt(preAtomo)!='('){
                                        preAtomo--;
                                        
                                    }
                                    preAtomo++;
                                    tempP=temporal.substring(preAtomo, i);
                                    pre=preAtomo;
                                    break;
                                }   
                            }
                        }
                        //Establece tempQ que es lo que esta despues del =
                        neg=i+1;
                        if(temporal.charAt(i+1)=='!'){
                            while(temporal.charAt(neg)=='!'){
                                neg++;
                            }
                        }
                        if(atomos.contains(String.valueOf(temporal.charAt(neg)))){
                            tempQ=temporal.substring(i+1,neg+1);
                            pos=neg;
                        }
                        else if(parentesisA.contains(String.valueOf(temporal.charAt(neg)))){
                            stack.push(temporal.charAt(neg));
                            for(int j=neg+1; !stack.isEmpty(); j++){
                                if(parentesisA.contains(String.valueOf(temporal.charAt(j)))){
                                    stack.push(temporal.charAt(j));
                                }
                                else if(parentesisC.contains(String.valueOf(temporal.charAt(j)))){
                                    stack.pop();
                                }
                                if(stack.isEmpty()){
                                    tempQ=temporal.substring(i+1, j+1);
                                    pos=j;
                                    break;
                                }   
                            }
                        }
                        total="((!"+tempP+"|"+tempQ+")&(!"+tempQ+"|"+tempP+"))";
                        temporal.replace(pre, pos+1, total);
                    } 
                
                
            }
            
            //System.out.println(temporal);
        return temporal.toString();
    }
    public static boolean pol(String e){
        for(int i=0; i<e.length(); i++){
            if(parentesisA.contains(String.valueOf(e.charAt(i)))){
                stack.push(e.charAt(i));
            }
            else if(parentesisC.contains(String.valueOf(e.charAt(i)))){
                if(!stack.isEmpty()){
                    while(!parentesisA.contains(String.valueOf(stack.peek()))){
                        if(operadores.contains(String.valueOf(stack.peek()))){
                            polaca+=stack.pop();
                            if(!parentesisA.contains(String.valueOf(stack.peek()))){
                                if((char)stack.peek()=='!'){
                                    //dskjhgflksdjhglskfjghsdkljfghdkljfghsdlkjghfsldkfjghlsdkjfghsdlfkgjhdkslfg
                                    polaca+=stack.pop();
                                }
                                else{
                                    System.out.println("No es una fórmula bien formada 53");
                                    return false;
                                }
                            }
                        }
                    }
                    if(stack.isEmpty()){
                        System.out.println("No es una fórmula bien formada 2");
                        return false;
                    }
                    else{
                        if(parentesisA.contains(String.valueOf(stack.peek()))){
                            stack.pop();
                            if(!stack.isEmpty()){
                                if((char)stack.peek()=='!'){
                                    polaca+=stack.pop();
                                }
                            }
                        }
                    }
                }
                else {
                    System.out.println("No es una fórmula bien formada 3");
                    return false;
                }
            }
            else if(operadores.contains(String.valueOf(e.charAt(i)))){
                if(e.charAt(i)=='!'){
                    if(i>=e.length()-1){
                        System.out.println("No es una fórmula bien formada 4");
                        return false;
                    }
                    if(atomos.contains(String.valueOf(e.charAt(i+1)))){
                        polaca+=e.charAt(i+1);
                        polaca+=e.charAt(i);
                        i++;
                    }
                    else if(parentesisA.contains(String.valueOf(e.charAt(i+1)))){
                        stack.push(e.charAt(i));
                    }
                    else{
                       System.out.println("No es una fórmula bien formada 43");
                        return false; 
                    }
                }
                else {
                    stack.push(e.charAt(i));
                }
            }
            else if(atomos.contains(String.valueOf(e.charAt(i)))){
                polaca+=e.charAt(i);
            }
        }
        if(!stack.isEmpty()){
            System.out.println("No es una fórmula bien formada 5"); 
            return false;
        }
        return true;
    }
    public static boolean arbol(){
        stack.clear();
        for(int i=0; i<polaca.length(); i++){
            if(atomos.contains(String.valueOf(polaca.charAt(i)))){
                Node leaf= new Node(polaca.charAt(i));
                stack.push(leaf);
            }
            if(polaca.charAt(i)=='!'){
                if(stack.isEmpty()){
                        System.out.println("No es una fórmula bien formada 6");
                        return false;
                    }
                Node leaf=new Node(polaca.charAt(i));
                leaf.setleft((Node)stack.pop());
                stack.push(leaf);
            }
            else{
                if(operadores.contains(String.valueOf(polaca.charAt(i)))){
                    Node leaf= new Node(polaca.charAt(i));
                    if(stack.isEmpty()){
                        System.out.println("No es una fórmula bien formada 7");
                        return false;
                    }
                    leaf.setleft((Node)stack.pop());
                    if(stack.isEmpty()){
                        System.out.println("No es una fórmula bien formada 8");
                        return false;
                    }
                    leaf.setright((Node)stack.pop());
                    stack.push(leaf);
                }
            }
            
        }
        if(stack.isEmpty()){
            System.out.println("No es una fórmula bien formada 9");
            return false;
        }
        cabeza=(Node)stack.pop();
        if(!stack.isEmpty()){
            System.out.println("No es una fórmula bien formada 10");
            return false;
        }
        return true;
    }
    public static boolean recorre(Node c){
        
        if(c.esHoja()){
            //System.out.println("hijos: ");
            //System.out.print(c.value);
            if(!atomos.contains(String.valueOf(c.value))){
                System.out.println("No es una fórmula bien formadaR1");
                return false;
            }
            else{
                if(!b.arrC.contains(c.value)){
                    b.arrC.add(c.value);
                }
            }
            
        }
        else{
            recorre(c.left);
            if(c.value!='!'){
                recorre(c.right);
            }
            if(c.value=='!' && c.right!=null){
                System.out.println("No es una fórmula bien formadaR2");
                return false;
            }
            if(!operadores.contains(String.valueOf(c.value))){
                System.out.println("No es una fórmula bien formadaR3");
                return false;
            }
            //System.out.print(c.value);
            //System.out.println("Operador: ");
            
        }
        return true;
    }
    
//CALCULATE Y EVALUAR SE USABAN PARA ARBOLES BINARIOS, YA NO SON NECESARIOS
    public static boolean calculate(Node l, char val, Node r){
        switch(val){
            case '&':
                return l.num&&r.num;
            case '|':
                return l.num||r.num;
            case '>':
                return !l.num||r.num;
            case '=':
                return (!l.num||r.num)||(!r.num||l.num);
            case '!':
                return !l.num;
        }
        
        return false;
    }
    public static boolean evaluar(Node n){
        int cambiarmanual;
        if (n != null)  {
            if (n.esHoja()){  // n is a node with a number
                cambiarmanual=(Integer)b.arrB.get(b.arrC.indexOf(n.value));
                if(cambiarmanual==1){
                     n.num=true;
                }
                else{
                    n.num=false;
                }
                return n.num;
            }
            else
            {
                n.left.num = evaluar(n.left);
                if(n.value!='!'){
                    n.right.num = evaluar(n.right);
                    n.num = calculate(n.left, n.value, n.right);
                }
                else {
                    n.num = calculate(n.left, n.value, null);
                }
            } //end else
        } //end if
        return n.num;
    } //end evaluate
    
    static public int letras(){
        int cont=0;
        for (int i = 0; i < 56; i++) {
            if(let[i]){
                
                cont++;
            }
        }
        return cont;
    }
    static public void convierte(int num){
        if(num==0)
            return;
        else{
            b.arrB.add(num%2);
            convierte(num/2);
        }
        
    }
    static public boolean cuentaEnBinario(){
        boolean chan=true;
        for (int i = 0; i < Math.pow(2, b.arrC.size())-1; i++) {
            //b.arrB.clear();
            convierte(i);
            for (int j = b.arrB.size(); j < b.arrC.size(); j++) {
                b.arrB.add(0);
            }
            chan=chan && evaluar(cabeza);
            if(!chan)
                return chan;
        }
        return chan;    
    }
    static public StringBuilder DosNeg(StringBuilder n){
        for(int i=0; i<n.length(); i++){
            if(n.charAt(i)=='!' && n.charAt(i+1)=='!'){
                n.delete(i, i+2);
            }
        }
        return n;
    }
    static public String inutiles(String s){
        //traducir erase_par
        StringBuilder temporal = new StringBuilder(s);
        int parentesis=0, i=0, j=0, k=0;
        boolean encontrado=false, eliminado=false;
        //System.out.println(temporal.toString());
        //System.out.println(temporal.length());
        while(i<temporal.length()){
            if(parentesisA.contains(String.valueOf(temporal.charAt(i)))){
                j=i;
                do{
                    if(parentesisC.contains(String.valueOf(temporal.charAt(j)))){
                        parentesis--;
                    }
                    else if(parentesisA.contains(String.valueOf(temporal.charAt(j)))){
                        parentesis++;
                    }
                    if(parentesis==0 && temporal.charAt(j)!='!' &&operadores.contains(String.valueOf(temporal.charAt(j)))){
                        encontrado=true;
                    }
                    if(parentesis == 0 && i > 0 && temporal.charAt(i-1)!='!' &&operadores.contains(String.valueOf(temporal.charAt(i-1)))) {
                        encontrado = true;
                    }
                    if(parentesis==0  &&parentesisC.contains(String.valueOf(temporal.charAt(j)))){
                        k=j+1;
                        while(!encontrado && temporal.length()>k && temporal.charAt(k)!=','){
                            if(parentesisC.contains(String.valueOf(temporal.charAt(k)))){
                                parentesis--;
                            }
                            else if(parentesisA.contains(String.valueOf(temporal.charAt(k)))){
                                parentesis++;
                            }
                            if(parentesis==0 && temporal.charAt(k)!='!' &&operadores.contains(String.valueOf(temporal.charAt(k)))){
                                encontrado=true;
                            }
                            k++;
                        }
                        if(!encontrado){
                            temporal.deleteCharAt(j);
                            temporal.deleteCharAt(i);
                            encontrado=true;
                            eliminado = true;
                        }
                    }
                    j++;
                }while(!encontrado && temporal.length()>j && temporal.charAt(j)!=',');
            }
            i++;
            if(eliminado) i--;
            eliminado = false;
            encontrado=false;
            parentesis=0;
        }
        return temporal.toString();
    }
    static public String desgloseString(String f){
        int a=0;
        String aux; //Para crear las hojas
        StringBuilder temporal = new StringBuilder(f);
        for(int i=0; i<temporal.length(); i++){
            if(f.charAt(i)==','){
                /*aux=temporal.substring(a, i);
                aux=inutiles(aux);
                aux=comas(aux);
                temporal.replace(i, i, aux)*/
            }
        }
        return f;
    }
    static public String morgan(String n){
        StringBuilder temporal= new StringBuilder(n);
        Stack newS = new Stack();
        do{
            for(int i=0; i<temporal.length(); i++){
            temporal=DosNeg(temporal);
            if(temporal.charAt(i)=='!'&& parentesisA.contains(String.valueOf(temporal.charAt(i+1)))){
                stack.push(temporal.charAt(i+1));
                for(int j=i+2; !stack.isEmpty(); j++){
                    if(atomos.contains(String.valueOf(temporal.charAt(j)))){
                        temporal.insert(j, '!');
                        j++;
                    }
                    else if(operadores.contains(String.valueOf(temporal.charAt(j)))&&temporal.charAt(j)!='!'){
                        switch(temporal.charAt(j)){
                            case '&':
                                temporal.replace(j, j+1, "|");
                                break;
                            case '|':
                                temporal.replace(j, j+1, "&");
                                break;
                        }
                        
                    }
                    else if(parentesisA.contains(String.valueOf(temporal.charAt(j)))){
                        temporal.insert(j, '!');
                        j++;
                        newS.push('(');
                        for(int w=j+1; !newS.isEmpty(); w++){
                            if(temporal.charAt(w)=='('){
                                newS.push('(');
                            }
                            else if(temporal.charAt(w)==')'){
                                newS.pop();
                                j=w;
                            }
                        }
                        
                    }
                    else if(parentesisC.contains(String.valueOf(temporal.charAt(j)))){
                        stack.pop();
                    }
                }
                temporal.deleteCharAt(i);
                i++;
            }
        }
        }while(temporal.indexOf("!(")>0);
        /*if(once){
            temporal.insert(1, "!(");
            temporal.insert(temporal.length()-1, ')');
            once=false;
        }*/
        return temporal.toString();
    }
    public static void main(String[] args) {
        Scanner kb=new Scanner(System.in);
        expresion=kb.nextLine();
        if(par(expresion)){
            if(pol(expresion)){
            //System.out.println(polaca);
                if(arbol()){
                    b = new Bin();
                    if(recorre(cabeza)){
                        if(validezConc(expresion)){
                            nueva=cambiaImpl(expresion);
                            nueva=cambiaDobImpl(nueva);
                            nueva=morgan(nueva);
                            nueva=morgan(nueva);
                            nueva=inutiles(nueva);
                            //System.out.println(nueva);
                            nueva=comas(nueva);
                            //System.out.println(nueva);
                            arbolComas(nueva);
                            desgloseArbol(head);
                            //System.out.println("Arbol Completo:");
                            //impArbol(head);
                            ramas(head);
                            validacion(head);
                            if(valor){
                                System.out.println("Válido.");
                            }
                            else{
                                System.out.println("No válido.");
                            }
                        }
                        /*System.out.println(nueva.length());
                        StringBuilder sb=new StringBuilder(nueva);
                        sb.insert(0,"(");
                        nueva = sb.toString();
                        nueva+=")";
                        System.out.println(nueva.length());
                        if(par(nueva)){
                           polaca="";
                            if(pol(nueva)){
                                //System.out.println(polaca);
                                if(arbol()){ 
                                     System.out.println("hola");
                    
                                }
                        
                            }
                        }*/
                    }
                }
            }
        }
        
    }
    
}

//DONE: Checar conclusión

//DONE: Negada, replace (:) por (&)
//???: Desglose String
//Árboles
// Crear el árbol con fórmulas separadas por comas
// Desglosar fórmulas en árbol creado
// Checar si hoja tiene string con 1 o 2 de length (átomo)
// Mandar a llamar ahí (en esa hoja) otra función que busque ese mismo átomo negado o no y cerrar
// Si lo encuentra, mandar a llamar ahí otra función para que inserte el cerrado en el último hijo de las ramas de abajo