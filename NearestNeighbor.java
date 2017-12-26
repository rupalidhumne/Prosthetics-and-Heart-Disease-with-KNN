import java.io.*;
import java.util.*;
public class NearestNeighbor
   {
       public static void main(String[] args) throws Exception
      {
        Scanner infile = new Scanner(new File("sampledata.txt"));
        Scanner kb = new Scanner(System.in);
        ArrayList<Instance> list = new ArrayList<Instance>(); //list of instances
        ArrayList<Instance> original = new ArrayList<Instance>(); //dataset
        ArrayList<Integer> responses=new ArrayList<Integer>(); //classification
        while(infile.hasNextLine())
         {
            String s = infile.nextLine();// skipped instances with questions marks or missing values
            if(!s.contains("?"))
               {
                  StringTokenizer st = new StringTokenizer(s,",");
                  while(st.hasMoreTokens())
                     {
                        Instance i = new Instance(Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Integer.parseInt(st.nextToken()));       
                        list.add(i); 
                            
                     }
               }      
         }
         System.out.println("Now you may enter a new data point");
         System.out.println();
         System.out.println("Age:");
         double a= Double.parseDouble(kb.next());
         System.out.println("Sex (0 for female 1 for male):");
         double b= Double.parseDouble(kb.next());
         System.out.println("Cp");
         double c= Double.parseDouble(kb.next());
         System.out.println("Trestbps:");
         double d= Double.parseDouble(kb.next());
         System.out.println("Chol");
         double e= Double.parseDouble(kb.next());
         System.out.println("Fbs");
         double f= Double.parseDouble(kb.next());
         System.out.println("Restecg");
         double g= Double.parseDouble(kb.next());
         System.out.println("Thalach");
         double h= Double.parseDouble(kb.next());
         System.out.println("Exang");
         double i= Double.parseDouble(kb.next());
         System.out.println("Oldpeak");
         double j= Double.parseDouble(kb.next());
         System.out.println("Slope");
         double k= Double.parseDouble(kb.next());
         System.out.println("Ca");
         double l= Double.parseDouble(kb.next());
         System.out.println("Thal");
         double m= Double.parseDouble(kb.next());
         Instance newInstance = new Instance(a,b,c,d,e,f,g,h,i,j,k,l,m,-1);
         sort(list,newInstance);      
         int kth = (int)(Math.sqrt(list.size()));      
         int[] nearestNeighbors = new int[kth];
         for(int x=0; x<nearestNeighbors.length; x++)
            {
               nearestNeighbors[x]=list.get(x).getResponse();
            }
         int classification=-1;
               double sum=0;
               for(int x=0; x<nearestNeighbors.length; x++)
                  {
                     sum+=nearestNeighbors[x];
                  }
               double length = 1.0*nearestNeighbors.length;
               classification=(int)Math.round(sum/nearestNeighbors.length);
               System.out.println(classification);
         newInstance.setResponse(classification);
         list.add(newInstance);
         original.add(newInstance);
         if(classification==0)
            {
               System.out.println("No heart burn for this patient!");
            }
         else if(classification==1)
            {
               System.out.println("A little");
            }
         else if(classification==2)
            {
               System.out.println("Somewhat");
            }
         else if(classification==3)
            {
               System.out.println("Be careful");
            }
         else if(classification==4)
            {
              System.out.println("lotta heartburn you got there");  
            } 
                
                        
      } 
   
      
      public static void sort(List<Instance> li, Instance i)
   {
      helper(li, 0, li.size() - 1, i);
   }
   private static void helper(List<Instance> li, int start, int end, Instance i)
   {
      int splitPt;
      if(start<end)
      {
         splitPt=split(li,start,end, i);
         helper(li,start,splitPt-1, i); //before pivot/left
         helper(li,splitPt+1,end,i); //after pivot/right
      }   
   }
   private static void swap(List<Instance> li, int a, int b)
   {
      Collections.swap(li, a, b);
   }
   private static int split(List<Instance> info, int first, int last,Instance i)
   {
                        
      int splitPt=first;
      double pivot=info.get(first).distance(i);
      while(first<=last)
      {
         if(info.get(first).distance(i)<=pivot) //checks to see if its on the right side
         {
            first++;  //like a for loop, it goes to the next cell
         }
         else if(info.get(last).distance(i)>=pivot)
         {
            last--;             //decrements the for loop or cell count thing
         }
         else
         {
            swap(info,first,last);  //if its on the wrong side then swap the two, increment first and decrement last
            first++;
            last--;
         }  
      }
      swap(info,last,splitPt);  //swaps pivot with actual center/element at splitPt
      splitPt=last;
      return splitPt;
   }

   /*
   public static void sort(List<Instance> list, Instance i)
   { 
      ArrayList<Instance> copyBuffer=new ArrayList<Instance>();
      mergeSortHelper(list, copyBuffer, 0, copyBuffer.size() - 1,i );
   }
   
   private static void mergeSortHelper(List<Instance> list, ArrayList<Instance> copyBuffer, int low, int high,Instance i)
   { 
      if (low < high)
      {
         int middle = (low + high) / 2;
         mergeSortHelper(list, copyBuffer, low, middle,i);
         mergeSortHelper(list, copyBuffer, middle + 1, high,i);
         merge(list, copyBuffer, low, middle, high,i);
      }
     
   }
   	
   public static void merge(List<Instance> list, ArrayList<Instance> copyBuffer, int low, int middle, int high,Instance instance) 
   {
        for(int x=low; x<=high; x++)
         {
            copyBuffer.set(x,list.get(x));  //transfers everything to copyBuffer
         }
       int i = low;
       int j=middle+1;  //create separate variables to test numbers while incremented
       int k=low;  
        
        while(i<=middle &&j<=high) //incrementer
      {
         if(copyBuffer.get(i).distance(instance)<=copyBuffer.get(j).distance(instance))  //actual variable
            {
               list.set(k,copyBuffer.get(i));
               i++;  //increments the low to the next one to check if its lower
            }
          else // if the next one isn't lower,copy the number over
            {
               list.set(k, copyBuffer.get(j)); 
               j++; 
            }
       k++; 
      }             
      while(i<=middle)  //copy the other stuff back
         {
            list.set(k, copyBuffer.get(i));
            k++;
            i++;
         }     
     
   } 
   */
     
  public static Integer[] mode(int a[])
  {
  List<Integer> modes = new ArrayList<Integer>();
  int maxCount=0;   
  for (int i = 0; i < a.length; ++i)
  {
    int count = 0;
    for (int j = 0; j < a.length; ++j)
    {
      if (a[j] == a[i]) ++count;
    }
    if (count > maxCount)
    {
      maxCount = count;
      modes.clear();
      modes.add( a[i] );
    } 
    else if ( count == maxCount )
    {
      modes.add( a[i] );
    }
  }
  return modes.toArray( new Integer[modes.size()] );
}
}            

   

          
         //add new instance to the list
         
 