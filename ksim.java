import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
public class ksim
{

   static DecimalFormat d = new DecimalFormat("0.00");
   public static void main(String[] args) throws FileNotFoundException
   {
      double startTime = System.nanoTime();
      Scanner kb = new Scanner(System.in);
      System.out.println("Enter the different types of motions");
      int t = kb.nextInt();
      System.out.println("Enter number of iterations per each motion");
      int m = kb.nextInt();
      System.out.println("Enter images per iteration");
      int i = kb.nextInt();
      int start = 0; 
      int end = i;
      ArrayList<Instancem> iterations = new ArrayList<Instancem>(); //iterations
      for(int j=0; j<t; j++)
      {
         start =0;
         end = i;
         for(int x=0; x<m; x++)
         {
         
            Double[][][] matrix = new Double[i][640][480]; 
            int f=0; //frame
            for(int a=start; a<end; a++)
            {
               String filename= "/Users/rdhumne/Documents/GMUInternship2015/Motion" + (j+1) + "_" + (a+1) + ".txt";
               System.out.println(filename);
               Scanner infile = new Scanner(new File(filename));
               for(int r=0; r<640; r++)
               {
                  for(int c=0;c<480; c++)
                  {
                     matrix[f][r][c]=infile.nextDouble();
                  }
               }
               f++;       
            }  
            Instancem instance = new Instancem(matrix,t);
            iterations.add(instance);
            start = i+start;
            end = i + end;
            
         }
      }   
      report(iterations);
      double endTime = System.nanoTime(); // end timer
      double duration = endTime - startTime;
      System.out.println("Runtime: " + duration);
   }
   public static void report(ArrayList<Instancem> iterations)
   {
      double[] accuracy = new double[10]; 
      ArrayList<ArrayList<Double>> precision = new ArrayList<ArrayList<Double>>();
      ArrayList<ArrayList<Double>> recall = new ArrayList<ArrayList<Double>>();
      for(int x=0; x<10; x++)
      {
         int[][] matrix = validate(iterations,x);
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
      double[] sumpres = new double[2]; //sum of precision arrays
      for(int x=0; x<10; x++)
      {
         ArrayList<Double> section = precision.get(x);
         for(int y=0; y<section.size(); y++)
         {
            sumpres[y]+=section.get(y);
         }   
      }
      for(int x=0; x<sumpres.length; x++)
      {
         System.out.println("Precision for classification #" + (x+1) + " : " + d.format(sumpres[x]/10));
      }
      double[] sumrecall = new double[2]; //sum of precision arrays
      for(int x=0; x<10; x++)
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
         System.out.println("Recall for classification #" +(x+1)+ ": " + d.format(sumrecall[x]/10));
      }
   
     
   }
   public static int[][] validate(ArrayList<Instancem> iterations, int x)
   {
      int matrix[][]=new int[2][2];
         Instancem test = iterations.get(x);
         int c= iterations.get(x).getS();
         int cl=0;
         if(c==1)
            {
               cl=0;
            }
         if(c==2)
            {
               cl=1;
            }         
         ArrayList<Instancem> train = new ArrayList<Instancem>();
         for(int a=0; a<iterations.size(); a++)
         {
            if(a!=x)
            {
               train.add(iterations.get(a));
            }
                        
         }
         int nn = nearestNeighbor(test, train);
         matrix[cl][nn]++;
                //finish       
      
      
      return matrix;
   }
   public static int nearestNeighbor(Instancem test, ArrayList<Instancem> train)
   {
      Double[][][] mat = test.getmat();
      ArrayList<Double> corrs = new ArrayList<Double>();
      ArrayList<Integer> ccs = new ArrayList<Integer>(); //classes
      Double[][] avg1 =test.task1();
      for(int x=0; x<train.size(); x++)
      {
         Double[][] avg2= train.get(x).task1();
         double d = correlation(avg1, avg2);
         corrs.add(d);
         ccs.add(train.get(x).getS()); //class corresponding to correlation value of test and train x
      }
      sort(corrs,ccs);
      ArrayList<Double> kcorrs = new ArrayList<Double>();
      ArrayList<Integer> kccs = new ArrayList<Integer>(); 
      for(int x=0; x<5; x++)
      {
         kcorrs.add(corrs.get(x));
         kccs.add(ccs.get(x));
      }
      int m= mode(kccs);  
      return m;
   
   }
   public static int mode(ArrayList<Integer> arr)
   {
      int oneval=0;
      int twoval=0;
      for(int x=0; x<arr.size(); x++)
      {
         if(arr.get(x)==0)
         {
            oneval++;
         }
         else
         {
            twoval++;
         }
      }
      if(oneval>twoval)
      {
         return 0;
      }
      else
      {
         return 1;
      }
   }                           
         
   public static void sort(ArrayList<Double>corrs, ArrayList<Integer> ccs)
   {
      helper(corrs, 0, corrs.size() - 1,ccs);
   }
   private static void helper(ArrayList<Double>corrs, int start, int end, ArrayList<Integer> ccs)
   {
      int splitPt;
      if(start<end)
      {
         splitPt=split(corrs,start,end,ccs);
         helper(corrs,start,splitPt-1, ccs); //before pivot/left
         helper(corrs,splitPt+1,end,ccs); //after pivot/right
      }   
   }
   private static void swap(ArrayList<Double> corrs, int a, int b,ArrayList<Integer> ccs)
   {
      Collections.swap(corrs,a,b);
      Collections.swap(ccs,a,b);
   }
   private static int split(ArrayList<Double>corrs, int first, int last,ArrayList<Integer> ccs)
   {
                        
      int splitPt=first;
      double pivot=corrs.get(first);
      while(first<=last)
      {
         if(corrs.get(first)<=pivot) //checks to see if its on the right side
         {
            first++;  //like a for loop, it goes to the next cell
         }
         else if(corrs.get(last)>=pivot)
         {
            last--;             //decrements the for loop or cell count thing
         }
         else
         {
            swap(corrs,first,last,ccs);  //if its on the wrong side then swap the two, increment first and decrement last
            first++;
            last--;
         }  
      }
      swap(corrs,last,splitPt,ccs);  //swaps pivot with actual center/element at splitPt
      splitPt=last;
      return splitPt;
   }
   public static double correlation(Double[][] avg1,Double[][] avg2)
   {
      return corr2(avg1, avg2);
   }
   public static double corr2(Double[][] a,Double[][] b)
   {
      double meanA=mean(a);
      double meanB=mean(b);
      double top = top(meanA, meanB, a, b);
      double bottom = bottom(meanA, meanB, a, b);  
      return top/bottom; 
   }
   public static double bottom(double ma, double mb, Double[][] a, Double[][] b)
   {
      double sumA=0;
      double sumB=0;
      for(int x=0; x<a.length; x++)
      {
         for(int y=0; y<a[0].length; y++)
         {
            sumA+=Math.pow((a[x][y]-ma),2);
         }
      } 
      for(int x=0; x<a.length; x++)
      {
         for(int y=0; y<a[0].length; y++)
         {
            sumB+=Math.pow((a[x][y]-mb),2);
         }
      }
      return Math.sqrt(sumA*sumB);
   }             
                        
   public static double mean(Double[][] arr)
   {
      double sum = 0;
      for(int x=0; x<arr.length; x++)
      {
         for(int y=0; y<arr[0].length; y++)
         {
            sum+=arr[x][y];
         }
      }
      return 1.0*(sum/(arr.length *arr[0].length));
   }
   public static double top(double ma, double mb, Double[][] a, Double[][]b)
   {
      double top = 0;
      for(int m=0; m<a.length; m++)
      {
         for(int n=0; n<a[0].length; n++)
         {
            top+= (a[m][n]-ma)*(b[m][n]*mb);
         }
      }
      return top;
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

