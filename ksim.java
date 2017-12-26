import java.io.File;
import java.util.*;


public class ksim //extends MyThread //iterations is not the same both times
{
	//public ksim( int f, int s, int e, int ith, int jth) 
	//{
		//super(f, s, e, ith, jth);
	//}
   static ArrayList<Instancem> iterations;
   public static void main(String[] args) throws Exception
   {
      double startTime = System.nanoTime();
      
      Scanner kb = new Scanner(System.in); //stops at motion1_657
      System.out.println("Enter images per iteration");
      int i = kb.nextInt();
      iterations = new ArrayList<Instancem>(); //iterations
       //List<Future<Instancem>> list = new ArrayList<Future<Instancem>>();
      for(int j=0; j<2; j++)
      {
         int start =0;
         int end = i;
         
         //ExecutorService executor = Executors.newFixedThreadPool(5); //5 motions at a time
         for(int x=0; x<5; x++)
         {
            Double[][][] matrix = new Double[i][480][640]; 
            int f=0;
            for(int a=start; a<end; a++)
            {
                  
               String filename= "/Users/Administrator/Documents/GMUInternship2015/Motion" + (j+1) + "_" + (a+1) + ".txt";
               System.out.println(filename);
               Scanner infile = new Scanner(new File(filename));
               for(int r=0; r<480; r++)
               {
                  for(int c=0;c<640; c++)
                  {
                     matrix[f][r][c]=infile.nextDouble();
                       	 
                  }
               } 
               f++;
               infile.close();
            }  
            start+=i;;
            end +=i; 
            iterations.add(new Instancem(matrix,j));
         }
         
      } 
      kb.close();
      int swap = (int)(Math.random()*1+3);
      for(int x=0; x<swap; x++)
      {
         Collections.swap(iterations, (int)(Math.random()*9), (int)(Math.random()*9));
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

   public static int[][] validate(ArrayList<Instancem> iterations, int x)
   {
      int matrix[][]=new int[2][2];
      Instancem test = iterations.get(x);
      int c= iterations.get(x).getS(); 
      ArrayList<Instancem> train = new ArrayList<Instancem>();
      for(int a=0; a<iterations.size(); a++)
      {
         if(a!=x)
         {
            train.add(iterations.get(a));
         }
                        
      }
      int nn = nearestNeighbor(test, train);
      matrix[c][nn]++;
                //finish       
      
      
      return matrix;
   }
   public static int nearestNeighbor(Instancem test, ArrayList<Instancem> train) throws NumberFormatException
   {
   
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
     // ArrayList<Double> kcorrs = new ArrayList<Double>();
      ArrayList<Integer> kccs = new ArrayList<Integer>(); 
      for(int x=0; x<6; x++)
      {
         //kcorrs.add(corrs.get(x));
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
         if(corrs.get(first)>=pivot) //changed less than symbol //checks to see if its on the right side
         {
            first++;  //like a for loop, it goes to the next cell
         }
         else if(corrs.get(last)<=pivot) //changed less than symbol
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
      return Math.abs(top/bottom); //changed to abs
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
            sumB+=Math.pow((b[x][y]-mb),2);
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
            top+= (a[m][n]-ma)*(b[m][n]-mb);
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

