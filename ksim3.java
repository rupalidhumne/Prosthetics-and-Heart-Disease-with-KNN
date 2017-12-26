import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
public class ksim3
{
     static DecimalFormat d = new DecimalFormat("0.00");
   public static void main(String[] args) throws FileNotFoundException
   {
      long startTime = System.nanoTime();
      Scanner kb = new Scanner(System.in);
      System.out.println("Enter number of motions");
      int m = kb.nextInt();
      System.out.println("Enter images per motion");
      int i = kb.nextInt();
      ArrayList<Instancem> motions = new ArrayList<Instancem>();
      for(int x=0; x<m; x++)
      {
         Double[][][] matrix = new Double[i][640][480]; //640 row 480 col i times
         for(int a=0; a<i; a++)
         {
            String filename = "Motion"+(x+1)+ "_" + (a+1)+".txt";
            Scanner infile = new Scanner(new File(filename));
            for(int r=0; r<640; r++)
            {
               for(int c=0;c<480; c++)
               {
                  matrix[a][r][c]=infile.nextDouble();
               }
            }       
         }
         Instancem instance = new Instancem(matrix, x);     
         motions.add(instance);
      }
     report(motions);
    long endTime = System.nanoTime(); // end timer
    long duration = endTime - startTime;
    System.out.println("Runtime:" + duration);
   }
   public static void report(ArrayList<Instancem> motions)
   {

	   double[] accuracy = new double[10]; 
	   /*
      double[] precision0 = new double[5]; 
      double[] precision1 = new double[5]; 
      double[] recall0 = new double[5]; 
      double[] recall1 = new double[5]; 
      */
      ArrayList<ArrayList<Double>> precision = new ArrayList<ArrayList<Double>>();
      ArrayList<ArrayList<Double>> recall = new ArrayList<ArrayList<Double>>();
      //int w=0;
      //int e=0;
      for(int x=0; x<10; x++)
      {
         int[][] matrix = validate(iterations,x);
         accuracy[x]=accuracy(matrix);
         precision.add(x,pres(matrix));
         recall.add(x,recall(matrix));          
      }
      /*
      for(int x=0; x<10; x++)
      {
         int[][] matrix = validate(iterations,x);
         accuracy[x]=accuracy(matrix);
         if(iterations.get(x).getS()==0)
         {
        	 precision0[e]=pres(matrix,0);
        	 recall0[e]=recall(matrix,0); 
        	 e++;
         }
         else
         {
        	 precision1[w]=pres(matrix,1);
             recall1[w]=recall(matrix,1); 
             w++;
         }
      }
      */
      double acsum=0;
      for(int x=0; x<accuracy.length; x++)
      {
         acsum+=accuracy[x];
      }
      double totaccuracy=(acsum/accuracy.length)*100;
      System.out.println("Accuracy:"+ totaccuracy);
      /*
      double sumpres0 = 0; //sum of precision arrays
      double sumpres1 =0;
      for(int x=0; x<5; x++)
      {
         sumpres0+=precision0[x];      
      }
      System.out.println("Precision for 0 is:"+(sumpres0/precision0.length));
      for(int x=0; x<5; x++)
      {
         sumpres1+=precision1[x];      
      }
      System.out.println("Precision for 1 is:"+(sumpres1/precision1.length));
      
      double sumRec0 = 0; //sum of precision arrays
      double sumRec1 =0;
      for(int x=0; x<5; x++)
      {
         sumRec0+=recall0[x];      
      }
      System.out.println("Recall for 0 is:"+(sumRec0/recall0.length));
      for(int x=0; x<5; x++)
      {
         sumRec1+=recall1[x];      
      }
      System.out.println("Recall for 1 is:"+(sumRec1/recall1.length));    
      */
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
         System.out.println("Precision for classification #" + (x+1) + " : " + (sumpres[x]/10));
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
         System.out.println("Recall for classification #" +(x+1)+ ": " + (sumrecall[x]/10));
      }
     
   }
   public static int[][] validate(ArrayList<Instancem> motions)
   {
      int matrix[][]=new int[2][2];
      for(int x= 0; x<motions.size(); x++)
      {
         Instancem test = motions.get(x);
         int c= motions.get(x).getS();
         ArrayList<Instancem> train = new ArrayList<Instancem>();
         for(int a=0; a<motions.size(); a++)
         {
            if(a!=x)
            {
               train.add(motions.get(a));
            }
                        
         }
         int nn = nearestNeighbor(test, train);
         matrix[c][nn]++;
                //finish       
      } 
     return matrix;
   }
   public static int nearestNeighbor(Instancem test, ArrayList<Instancem> train)
   {
      Double[][][] mat = test.getmat();
      ArrayList<Double> corrs = new ArrayList<Double>();
      ArrayList<Integer> ccs = new ArrayList<Integer>(); //classes
      Double[][] avg1 =test.task3();
      for(int x=0; x<train.size(); x++)
      {
         Double[][] avg2= train.get(x).task3();
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
   public static double corr2(Double[][] a, Double[][] b)
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
   /*
   public static double pres(int[][] matrix,int x)
   {
      //double[] thing = new double[5];      
      double tp = tp(matrix);
      double fp = fp(matrix,x);
      double precision =0;
      if(tp==0&fp==0)
      {
         precision=0;
      }
      else
      {   
         precision = (tp/(fp+tp))*100;
      }   
      return precision;    
   } 
   public static double recall(int[][]matrix,int x)
   {          
      double tp = tp(matrix);
      double fn = fn(matrix,x);
      double recall = (tp/(fn+tp))*100;
          
      
      return recall;
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
   */
   public static ArrayList<Double> pres(int[][] matrix)
   {
      ArrayList<Double> thing = new ArrayList<Double>();
      for(int x=0; x<matrix.length; x++)
      {
         double tp = tp(matrix);
         double fp = fp(matrix,x);
         if(tp==0 && fp==0)
         {
        	 thing.add(x,100.0);
         }
         else if(tp==1&& fp==0)
         {
        	 thing.add(x,100.0);
         }
         else
         {
        	 double precision = (tp/(fp+tp))*100;
        	 thing.add(x,precision);
         }
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
         if(tp==0 && fn==0)
         {
             thing.add(x,100.0);  
         }
         else if(tp==1&& fn==0)
         {
        	 thing.add(x,100.0);
         }
         else
         {
             double recall = (tp/(fn+tp))*100;
             thing.add(x,recall); 
         }

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

