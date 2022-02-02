import java.util.*;
import java.io.*;
import java.lang.*;
public class maxminheap{
	private ArrayList<Integer> max_heap;
	private ArrayList<Integer> min_heap;
	private int buffer;
    private int min=-1,max=-1;
	
	public maxminheap(){  
	    max_heap=new ArrayList<Integer>();
        min_heap =new ArrayList<Integer>();
        buffer=-1;
	}
    public void initialize(String in_path){
        try{
            File myobj= new File(in_path);// reading the file
            Scanner myreader = new Scanner(myobj); //scanning content
            String out=in_path;
            String f=myobj.getName();
            out=out.replaceAll(f,"result_"+f);
            execution(in_path,out);
        }
        catch(FileNotFoundException e){
            System.out.println("file not found!");
        }
    }

	public void execution(String in_path, String out_path){
        try{
            File myobj= new File(in_path);// reading the file
            Scanner myreader = new Scanner(myobj); //scanning content
            while (myreader.hasNextLine()){
                String data= myreader.nextLine();
                String [] w=data.split(" ");//removing spaces between words
                if(w[0].equals("construct")){
                    int a=Integer.parseInt(w[1]);//scan size
                    data=myreader.nextLine();//scan values line
                    w=data.split(" ");//remove space between numbers
                    if (a %2!=0){//if size is odd add last item to buffer
                        buffer=Integer.parseInt(w[a-1]);
                        a=a-1;
                    }
                    for (int i=0;i<a;i++){
                        if(i<a/2){
                            min_heap.add(Integer.parseInt(w[i]));//adding half of numbers to minheap
                        }
                        else{
                            max_heap.add(Integer.parseInt(w[i]));//adding the other half to maxheap
                        }
                    }
                    heapConstruction();
                    save(out_path);
                   
                }//end construct
                else if(w[0].equals("insert")){
                    int a=Integer.parseInt(w[1]);//scan number to be inserted
                    insertItem(a);
                    save(out_path);
                    
                }//end insert
                else if(w[0].equals("removeMin")){
                    min=removeMin();//saving min to global variable
                    save(out_path);
                    min=-1;//re-intializing min
                    
                }//end removeMin
                else if(w[0].equals("removeMax")){
                    max=removeMax();//saving max to global variable
                    save(out_path);
                    max=-1;//re-initializing max
                    
                }//end removeMax
                if(myreader.hasNextLine()) myreader.nextLine();//to remove blank line
            }//end while
        }//try
        catch(FileNotFoundException e){
            System.out.println("operation failed!");
        }//catch
    }

	private void heapConstruction(){//arranging numbers in min and max heaps
        int size=min_heap.size();
        int totallevels=(int)Math.ceil(Math.log(size+1)/Math.log(2));// total levels
        int external=(size/2) +1;//number of external elemenets
        for(int j=external-1;j<size;j++){ //swapping external elements if possible
            if(min_heap.get(j)>max_heap.get(j)){
                int tmp=min_heap.get(j);
                min_heap.set(j,max_heap.get(j));
                max_heap.set(j,tmp); 
            }
        }
        
        for (int i=totallevels-1;i>=1;i--){
            int node=(int)Math.pow(2,i-1);//number of nodes at each level  
            
            for(int j=i;j<(i+node);j++){  
                toMaxHeapify(j,i+1);//down heapify for items in minheap at level i and making upheap for maxheap up to level i+1
            }
            for(int j=i;j<(i+node);j++){
                toMinHeapify(j ,i);//down heapify for items in maxheap at level i and making upheap for minheap up to level i
            }
        }
	}

	private void toMaxHeapify(int i,int level){
        int size=min_heap.size();
        int external=(size/2) +1;
        if(i<external){//if i has children
            int l=2*i;
            int r=2*i+1;
            if(min_heap.get(i-1)>min_heap.get(l-1) || (r<=size && min_heap.get(i-1)>min_heap.get(r-1))){//if one of children is less than parent
                if(r<=size){//if i has left and right children
                 if(min_heap.get(l-1)<min_heap.get(r-1)){// if left is less than right
                    int tmp=min_heap.get(l-1);//we replace parent with it
                    min_heap.set(l-1,min_heap.get(i-1));
                    min_heap.set(i-1,tmp);
                    toMaxHeapify(l,level);//recurssive call for left child
                }//enf if
                else{//if right is less than left
                    int tmp=min_heap.get(r-1);//we replace parent with it
                    min_heap.set(r-1,min_heap.get(i-1));
                    min_heap.set(i-1,tmp);
                    toMaxHeapify(r,level);//recurssive call for right child
                }//end else
                }
                else{//to handle cases where i has only left child
                    int tmp=min_heap.get(l-1);
                    min_heap.set(l-1,min_heap.get(i-1));
                    min_heap.set(i-1,tmp);
                    //toMaxHeapify(l,level);
                }//end else
            }//ehd if
            toMaxHeapify(i+1,level);
        }//end if
        else{//i is an external node
            for(int j=external-1;j<size;j++){//swap associated nodes if possible
                if(min_heap.get(j)>max_heap.get(j)){
                    int tmp=min_heap.get(j);
                    min_heap.set(j,max_heap.get(j));
                    max_heap.set(j,tmp); 
                    upheap(2,j+1,level);//upheap max heap for swapped element
                }//end if
            }//end for
        }//end else
		
	}
    
	public void toMinHeapify(int i,int level){//same as max heap but in opposite manner
        int size=max_heap.size();
        int external=(size/2) +1;
        if(i<external){
            int l=2*i;
            int r=2*i+1;
            if(max_heap.get(i-1)<max_heap.get(l-1) || (r<=size && max_heap.get(i-1)<max_heap.get(r-1))){
                if(r<=size){
                 if(max_heap.get(l-1)>max_heap.get(r-1)){
                    int tmp=max_heap.get(l-1);
                    max_heap.set(l-1,max_heap.get(i-1));
                    max_heap.set(i-1,tmp);
                    toMinHeapify(l,level);
                 }
                 else{
                    int tmp=max_heap.get(r-1);
                    max_heap.set(r-1,max_heap.get(i-1));
                    max_heap.set(i-1,tmp);
                    toMinHeapify(r,level);
                 }
                }
                else{
                    int tmp=max_heap.get(l-1);
                    max_heap.set(l-1,max_heap.get(i-1));
                    max_heap.set(i-1,tmp);
                }
            }
            toMinHeapify(i+1,level);
            
        }
        else{
            for(int j=external-1;j<size;j++){
                if(min_heap.get(j)>max_heap.get(j)){
                    int tmp=min_heap.get(j);
                    min_heap.set(j,max_heap.get(j));
                    max_heap.set(j,tmp); 
                    upheap(1,j+1,level);
                }
            }
        }
	}
    private void upheap(int a,int i,int l){
        int size=min_heap.size();
        int totallevels=(int)Math.ceil(Math.log(size+1)/Math.log(2));
        int parent=i/2;
        if(a==1){//if heap is min heap
            if(l>=totallevels || parent==0) return;//if we done heapifying till level l
            if(min_heap.get(i-1)>min_heap.get(parent-1)) return;//if parent less than child we finish the function 
            int tmp=min_heap.get(i-1);//else we switch 
            min_heap.set(i-1,min_heap.get(parent-1));
            min_heap.set(parent-1,tmp);
            upheap(a,parent,l+1);//recurssive call to parent after swapping
        }
        else{//if heap is max heap
            if(l>=totallevels || parent==0) return;
            if(max_heap.get(i-1)<max_heap.get(parent-1)) return;//if parent greater than child we finish the function
            int tmp=max_heap.get(i-1);//else swap
            max_heap.set(i-1,max_heap.get(parent-1));
            max_heap.set(parent-1,tmp);
            upheap(a,parent,l+1);//recurssive call for parent after swapping

        }
    }
	
	public int removeMin(){
        int a=0;
        int size=max_heap.size();
        if(buffer!=-1){//if buffer not empty
            if(buffer<=min_heap.get(0)) {//if buffer less than root oif min heap
                a=buffer;
                buffer=-1;//empty buffer and return its valus
                return a;
            }
            else{//if buffer greater than root of min heap
                a=min_heap.get(0);
                min_heap.set(0,buffer);//set buffer as a root of min heap
                buffer=-1;// empty buffer
                toMaxHeapify(1,1);// heapify min heap
                return a;//return root of min heap
            }
        }
        else{
            a=min_heap.get(0);
            int c=min_heap.get(size-1);
            buffer=max_heap.get(size-1);//set buffer equal the last element of max heap
            min_heap.set(0,c)   ;//set root of min heap the last element of min heap
            max_heap.remove(size-1);//remove last element of max heap
            min_heap.remove(size-1);//remove last element of max heap
            toMaxHeapify(1,1);//heapify
            return a;//return root of min heap
        }
    }
	
	public int removeMax(){//same as removeMax but in opposite manner
        int a=0;
        int size=min_heap.size();
        if(buffer!=-1){
            if(buffer>=max_heap.get(0)) {
                a=buffer;
                buffer=-1;
                return a;
            }
            else{
                a=max_heap.get(0);
                max_heap.set(0,buffer);
                buffer=-1;
                toMinHeapify(1,1);
                return a;
            }
        }
        else{
            a=max_heap.get(0);
            int c=max_heap.get(size-1);
            buffer=min_heap.get(size-1);;
            max_heap.set(0,c);
            max_heap.remove(size-1);
            min_heap.remove(size-1);
            toMinHeapify(1,1);
            return a;
        }
    }  
	public void insertItem(int ele){
        if(buffer==-1){// if buffer is empty put element in buffer
            buffer=ele;
        }
        else{
            if(ele<buffer){//if buffer greater than element 
                max_heap.add(ele);
                min_heap.add(buffer);
                buffer=-1;
            }
            else{//if element greater than buffer
                min_heap.add(ele);
                max_heap.add(buffer);
                buffer=-1;
            }
            int size=max_heap.size();
            toMaxHeapify(size,1);//upheap for max heap
            toMinHeapify(size,1);//upheap for min heap 
        }

    } 

	private void save(String out_path){//saving data to a file
        try{
            File myobj = new File(out_path);
            myobj.createNewFile();//create file or overwrite if already exists
            FileWriter f= new FileWriter(myobj,true);
            if(max!=-1) f.write("max output "+ Integer.toString(max) +"\n");//if max has a value
            if(min!=-1) f.write("min output "+ Integer.toString(min) +"\n");//if min has a valus
            f.write("max heap");//writing values in max heap
            for(int i=0; i<max_heap.size();i++) f.write(" "+Integer.toString(max_heap.get(i)));
            f.write("\n"+"min heap");//writing values in min heap
            for(int i=0; i<max_heap.size();i++) f.write(" "+Integer.toString(min_heap.get(i)));
            if (buffer == -1) f.write("\n"+"buffer"+"\n\n");//writing buffer
            if(buffer!=-1) f.write("\n"+"buffer"+" "+Integer.toString(buffer) +"\n\n");
            f.close();
        }//end try
        catch(IOException e){
            System.out.println("operation failed!");
        }//end catch
    } 

	public static void main(String args[]){
		maxminheap heap = new maxminheap();
        System.out.println("please enter file path:");
        Scanner a=new Scanner(System.in);
        String in=a.nextLine();
        heap.initialize(in);
	}
}