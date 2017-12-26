import java.io.*;
import java.util.*;
public class NearestNeighborClass
{
   private String filename;
   private static int k;
   private static List<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
      
   public NearestNeighborClass(String s, int kth) throws FileNotFoundException
   {
      filename=s;
      k=kth;
      this.makeData();
            
   }
   public void makeData() throws FileNotFoundException
   {
      Scanner infile = new Scanner(new File(filename));      
      while(infile.hasNextLine())
      {
         String s = infile.nextLine();
         if(!s.contains("?"))
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
   }
   public static String classify(ArrayList<String> ats)
   {           
       
       sort(ats);
      double[] responses = nearestNeighbors();
      int sum = 0;
      
      int[] vals = new int[responses.length];
      for(int x=0; x<vals.length; x++)
         {
            int q = (int)responses[x];
            vals[x]=q;
         }   
      int classification = mode(vals);
   /*  
      for(int x=0; x<responses.length; x++)
      {
         sum+=responses[x];
      }
      double length = 1.0*responses.length;
      int classification=(int)Math.round(sum/responses.length);
   */   
      return ""+ classification;
   }

   public static int mode(int[] n)
      {
         int maxKey =0;
         int maxCounts=0;
         int[]counts=new int[n.length];
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
                   
   public static void sort(ArrayList<String> i)
   {
      helper(0, list.size() - 1, i);    
   }
   public static Double distance(int index, ArrayList<String> attributes)
   {
      double distance =  0.0;
      ArrayList<Double> datapt = list.get(index);
      for(int y=0; y<datapt.size()-1; y++)//everything but the response (last index)
      {
         distance+=Math.pow((datapt.get(y)-Double.parseDouble(attributes.get(y))),2);
      }   
      return Math.sqrt(distance);
   }
               
   private static void helper(int start, int end, ArrayList<String> ats)
   {
      int splitPt;
      if(start<end)
      {
         splitPt=split(start,end,ats);
         helper(start,splitPt-1, ats); 
         helper(splitPt+1,end,ats); 
      }   
   }
   private static int split(int first, int last,ArrayList<String> attributes)
   {
                        
      int splitPt=first;
      double pivot=distance(first, attributes);
      while(first<=last)
      {
         if(distance(first, attributes)<=pivot) //checks to see if its on the right side
         {
            first++;  //like a for loop, it goes to the next cell
         }
         else if(distance(last,attributes)>=pivot)
         {
            last--;             //decrements the for loop or cell count thing
         }
         else
         {
            swap(first,last);  //if its on the wrong side then swap the two, increment first and decrement last
            first++;
            last--;
         }  
      }
      swap(last,splitPt);  //swaps pivot with actual center/element at splitPt
      splitPt=last;
      return splitPt;
   }
   private static void swap(int a, int b)
   {
     Collections.swap(list, a, b);
   }
   public static double[] nearestNeighbors()
   {
     double[] responses = new double[k]; 
      for(int x=0; x<responses.length; x++)
      {
         ArrayList<Double> datapt = list.get(x);

         responses[x]= datapt.get(datapt.size()-1);
      } 
      return responses;           
   }       
       
            
}