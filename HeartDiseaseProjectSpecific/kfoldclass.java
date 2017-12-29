import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
public class kfoldclass
{
   private String filename;
   private static int k;
   private static ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
   DecimalFormat d = new DecimalFormat("0.00");
   boolean clasString;
  TreeSet<Double> set = new TreeSet<Double>();
  
   public kfoldclass(String s, int kth) throws FileNotFoundException
   {
      filename=s;
      k=kth;
      this.makeData();
   }
             
   public int makeData() throws FileNotFoundException //returns the number of possible classifications
   {
      Scanner infile = new Scanner(new File(filename));   
   while(infile.hasNextLine())
      {
         String s = infile.nextLine();
         if(!s.contains("?") && (!s.contains("+")))
         {
            StringTokenizer st = new StringTokenizer(s, ",");
            ArrayList<Double> instance = new ArrayList<Double>();
            int x=0;
               
            while(st.hasMoreTokens())
            {
               instance.add(x,Double.parseDouble(st.nextToken())); 
               x++; 
            }
            list.add(instance);
         }
       }  
      for(int x=0; x<list.size(); x++)
      {
         set.add(list.get(x).get(list.get(x).size()-1));
      }  
      return set.size();
   }
   public ArrayList<ArrayList<ArrayList<Double>>> fill()
   {
      int sectionsize = list.size()/k;
      int startval=0;
      int endval=sectionsize;
      ArrayList<ArrayList<ArrayList<Double>>> groups = new ArrayList<ArrayList<ArrayList<Double>>>();
      for(int x=0; x<k; x++)
      {
         ArrayList<ArrayList<Double>> section = new ArrayList<ArrayList<Double>>();
         for(int y=startval; y<endval; y++)
         { 
            section.add(list.get(y)); //adds the arraylist aka instance in list to section
         }
         groups.add(section);
         startval=endval;
         endval+=sectionsize;
      }
      return groups;
   }
   public void report() throws FileNotFoundException
   {
      int possibleclassifications = makeData();
      ArrayList<ArrayList<ArrayList<Double>>> stack = fill();
      double[] accuracy = new double[k]; //accuracy, each cell has accuracy for the kth section
      ArrayList<ArrayList<Double>> precision = new ArrayList<ArrayList<Double>>();
      ArrayList<ArrayList<Double>> recall = new ArrayList<ArrayList<Double>>();
      for(int x=0; x<k; x++)
      {
         int[][] matrix = validate(stack,x, possibleclassifications);
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
      double[] sumpres = new double[possibleclassifications]; //sum of precision arrays
      for(int x=0; x<k; x++)
      {
         ArrayList<Double> section = precision.get(x);
         for(int y=0; y<section.size(); y++)
         {
            sumpres[y]+=section.get(y);
         }   
      }
      for(int x=0; x<sumpres.length; x++)
      {
         System.out.println("Precision for classification #" + (x+1) + " : " + d.format(sumpres[x]/k));
      }
      double[] sumrecall = new double[possibleclassifications]; //sum of precision arrays
      for(int x=0; x<k; x++)
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
         System.out.println("Recall for classification #" +(x+1)+ ": " + d.format(sumrecall[x]/k));
      }
   
   
   }

   public int[][] validate(ArrayList<ArrayList<ArrayList<Double>>> stack,int x, int classifications)
   {
      int matrix[][]=new int[classifications][classifications];
      ArrayList<ArrayList<Double>> test = stack.get(x);
      ArrayList<ArrayList<Double>> train=new ArrayList<ArrayList<Double>>();
      for(int i=0; i<stack.size(); i++)
      {
         if(i!=x)
         {
            ArrayList<ArrayList<Double>> section = stack.get(i); 
            for(int w=0; w<section.size(); w++)
            {
               train.add(section.get(w));
            }
         }
      }
      for(int a=0; a<test.size(); a++)
      {
         int size = test.get(a).size()-1;
         double d = test.get(a).get(size); //gets the last value
         int ce = (int)(d); //get response, aka last val in an arraylist acting as an  instance
         int c=0;  
         Iterator<Double> ite = set.iterator();
         ArrayList<Double> cols = new ArrayList<Double>(); //predicted, cols
         int i=0;
         cols.addAll(set);      
         for(int y=0; y<cols.size(); y++)
            {
               if((cols.get(y)*1.0)==ce)
                  {
                     c=y;
                  }
            }         
         int re=nearestNeighbor(test.get(a), train); //actual, rows
         int r=0;
         ArrayList<Double> rows = new ArrayList<Double>(); 
         rows.addAll(set); 
          for(int y=0; y<rows.size(); y++)
            {
               if((rows.get(y)*1.0)==re)
                  {
                     r=y;
                  }
            } 
         Double y = new Double(r);
         int s = y.intValue(); 
          Double z = new Double(c);
         int t = z.intValue();         
         matrix[s][t]++;  
      }   
   
      return matrix;
   }      
   public static int nearestNeighbor(ArrayList<Double>newInstance, ArrayList<ArrayList<Double>> li)
   {
      sort(newInstance, li);      
               
      double[] nearestNeighbors = new double[k];
      for(int x=0; x<nearestNeighbors.length; x++)
      {
         int size = li.get(x).size()-1;
         nearestNeighbors[x]=li.get(x).get(size);
      }
      int[] vals = new int[nearestNeighbors.length];
      for(int x=0; x<vals.length; x++)
      {
         int q = (int)nearestNeighbors[x];
         vals[x]=q;
      }   
      int classification = mode(vals);
   
      newInstance.set(newInstance.size()-1,(double)classification);
      li.add(newInstance);
      return classification;
   }
   public static int mode(int[] n)
   {
      int maxKey =0;
      int maxCounts=0;
      int[]counts=new int[n.length *10];
      for(int i=0; i<n.length; i++)
      {
         counts[n[i]]++;
         if(maxCounts<counts[n[i]])
         {
            maxCounts=counts[n[i]];
            maxKey=n[i];
         }
      }
      return maxKey;
   }               
   public static void sort(ArrayList<Double>newInstance, ArrayList<ArrayList<Double>> li)
   {
      helper(li, 0, li.size() - 1, newInstance);
   }
   private static void helper(ArrayList<ArrayList<Double>> li, int start, int end, ArrayList<Double> i)
   {
      int splitPt;
      if(start<end)
      {
         splitPt=split(li,start,end, i);
         helper(li,start,splitPt-1, i); //before pivot/left
         helper(li,splitPt+1,end,i); //after pivot/right
      }   
   }
   private static void swap(ArrayList<ArrayList<Double>> info, int a, int b)
   {
      Collections.swap(info, a, b);
   }
   private static int split(ArrayList<ArrayList<Double>> info, int first, int last,ArrayList<Double> i)
   {
                        
      int splitPt=first;
      double pivot=distance(info, first, i);
      while(first<=last)
      {
         if(distance(info, first, i)<=pivot) //checks to see if its on the right side
         {
            first++;  //like a for loop, it goes to the next cell
         }
         else if(distance(info, last, i)>=pivot)
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
   public static double distance(ArrayList<ArrayList<Double>> info,int index, ArrayList<Double> attributes)
   {
      double distance =  0.0;
      ArrayList<Double> datapt = info.get(index);
      for(int y=0; y<datapt.size()-1; y++)//everything but the response (last index)
      {
         distance+=Math.pow((datapt.get(y)-attributes.get(y)),2);
      }   
      return Math.sqrt(distance);
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
      for(int x=0; x<matrix.length; x++)
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
      for(int x=0; x<matrix.length; x++)
      {
         double tp = tp(matrix);
         double fn = fn(matrix,x);
         double precision = (tp/(fn+tp))*100;
         thing.add(x,precision); 
      }
      return thing;
   }    

      
   public static double fp(int[][]matrix,int x) //BLAH
   {
      double sum =0.0;
      //System.out.println(matrix.length);
      for(int y=0; y<matrix.length; y++)
      {
         if(y!=x)
         {
            sum+=matrix[y][x];
         }
      }
      return sum;
   }                  
   public static double fn(int matrix[][], int x) //BLAH
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
              

}   


            

   
               
            
      

