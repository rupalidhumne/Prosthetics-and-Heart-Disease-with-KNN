import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
public class fivefold
   {
       public static void main(String[] args) throws Exception
      {
        DecimalFormat d = new DecimalFormat("0.00");
        Scanner infile = new Scanner(new File("sampledata.txt"));
        Scanner kb = new Scanner(System.in);
        ArrayList<Instance> list = new ArrayList<Instance>(); //list of instances
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
         int sections = list.size()/5;
         ArrayList<Instance> one = new ArrayList<Instance>();
         ArrayList<Instance> two = new ArrayList<Instance>();
         ArrayList<Instance> three = new ArrayList<Instance>();
         ArrayList<Instance> four = new ArrayList<Instance>();
         ArrayList<Instance> five = new ArrayList<Instance>();
         for(int x=0; x<sections; x++)
            {
               one.add(list.get(x));
            }   
         for(int x=0; x<sections; x++)
            {
               two.add(list.get(x));
            }
         for(int x=0; x<sections; x++)
            {
               three.add(list.get(x));
            }
         for(int x=0; x<sections; x++)
            {
               four.add(list.get(x));
            }
         for(int x=0; x<sections; x++)
            {
               five.add(list.get(x));
            }
      ArrayList<ArrayList<Instance>> stack = new ArrayList<ArrayList<Instance>>(5);
      stack.add(one);
      stack.add(two);
      stack.add(three);
      stack.add(four);
      stack.add(five);
      double[] accuracy = new double[5];
      ArrayList<ArrayList<Double>> precision = new ArrayList<ArrayList<Double>>();
      ArrayList<ArrayList<Double>> recall = new ArrayList<ArrayList<Double>>();
  
      for(int x=0; x<5; x++)
         {
            int[][] matrix = validate(stack,x);
            accuracy[x]=accuracy(matrix);
            precision.add(x,pres(matrix));
            recall.add(x,recall(matrix));          
         } 
       double acsum=0;
       for(int x=0; x<accuracy.length; x++)
         {
            acsum+=accuracy[x];
         }
      double totaccuracy=(acsum/accuracy.length)*100;
      System.out.println("Accuracy:"+ d.format(totaccuracy));
      double[] sumpres = new double[5]; //sum of precision arrays
      for(int x=0; x<5; x++)
         {
            ArrayList<Double> section = precision.get(x);
            for(int y=0; y<section.size(); y++)
               {
                  sumpres[y]+=section.get(y);
               }   
         }
      for(int x=0; x<sumpres.length; x++)
         {
            System.out.println("Precision for " +x+ ": " + d.format(sumpres[x]/sumpres.length));
         }
     
     double[] sumrecall = new double[5]; //sum of precision arrays
      for(int x=0; x<5; x++)
         {
            ArrayList<Double> section = recall.get(x);
            for(int y=0; y<section.size(); y++)
               {
                  sumrecall[y]+=section.get(y);
               }   
         }
      System.out.println();
      for(int x=0; x<sumrecall.length; x++)
         {
            System.out.println("Recall for " +x+ ": " + d.format(sumrecall[x]/sumrecall.length));
         }
             
           
                
}
         
   public static int[][] validate(ArrayList<ArrayList<Instance>> stack,int x)
   {
      int matrix[][]=new int[5][5];
      ArrayList<Instance> test = stack.get(x);
      ArrayList<Instance> train=new ArrayList<Instance>();
      for(int i=0; i<stack.size(); i++)
         {
            if(i!=x)
               {
                 ArrayList<Instance> section = stack.get(i); 
                  for(int w=0; w<section.size(); w++)
                     {
                        train.add(section.get(w));
                     }
               }
         }
       for(int a=0; a<test.size(); a++)
         {
            int r = test.get(a).getResponse();
            int c=nearestNeighbor(test.get(a), train);
            matrix[r][c]++;
         }
      return matrix;
   }      
            
 public static double accuracy(int[][]matrix)
   {
      double accuracy=0.0;
      double total=matrixsum(matrix);
      double tp = tp(matrix);
      accuracy = tp/total;
      return accuracy;
   }
 public static ArrayList<Double> pres(int[][] matrix)
   {
      ArrayList<Double> thing = new ArrayList<Double>();
      for(int x=0; x<5; x++)
         {
            double tp = tp(matrix);
            double fp = fp(matrix,x);
            double precision = (tp/(fp+tp))*100;
            thing.add(x,precision);
         }
      return thing;    
   }        

public static ArrayList<Double> recall(int[][]matrix)
   {          
       ArrayList<Double> thing = new ArrayList<Double>(); 
      for(int x=0; x<5; x++)
         {
            double tp = tp(matrix);
            double fn = fn(matrix,x);
           double precision = (tp/(fn+tp))*100;
           thing.add(x,precision); 
         }
       return thing;
   }    

      
 public static double fp(int[][]matrix,int x)
   {
       double sum =0.0;
        for(int y=0; y<matrix.length; y++)
         {
            if(y!=x)
               {
                  sum+=matrix[y][x];
               }
         }
       return sum;
   }                  
  public static double fn(int matrix[][], int x)
   {
       double sum =0.0;
        for(int y=0; y<matrix[0].length; y++)
         {
            if(y!=x)
               {
                  sum+=matrix[x][y];
               }
         }
       return sum;
   }               
            
            
 public static double matrixsum(int[][] matrix)
   {
      double sum = 0.0;
      for(int x=0; x<matrix.length; x++)
         {
            for(int y=0; y<matrix[0].length; y++)
               {
                  sum+=matrix[x][y];
               }
         }
      return sum;
   }
  public static double tp(int[][]matrix)
   {
      double sum = 0.0;
      for(int x=0; x<matrix.length; x++)
         {
            for(int y=0; y<matrix[0].length; y++)
               {
                  if(x==y)
                     {
                        sum+=matrix[x][y];
                     }   
               }
         }
      return sum;
   }   
              
                               
                     
public static int nearestNeighbor(Instance newInstance, List<Instance> list)
   {
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
         newInstance.setResponse(classification);
         list.add(newInstance);
        return classification;
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


} 
 