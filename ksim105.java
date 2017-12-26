import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
public class ksim105
{
   
   static ArrayList<Instancem> iterations;
  
   static final int nRows = 480;
   static final int nCols= 640;
   
   static final int nCoords=30625;
   static final int nCoordsSize=175;
   
   //static final int nSubjects = 6;
   static final int nActions = 8;
   static final int nIterations =5 ;

   static int nFrames=28; //# frames = # files
   
   static final String dataFile="Motion";
   static final String sString= "S";
   
   static String filename;
   static File file;

   static double corrTime, patternTime, classTime;
   static int[] freqTested;
   
   
   public static void main(String[] args) throws Exception //if still OOM subtract as you bring it in
   {
      ArrayList<Coord> coordArray = new ArrayList<Coord>();
      int r=0; 
      int c=0;
      for(int x=0; x<nCoords; x++) //generating nCoords coordinates
      {
         r = (int)(Math.random()*nRows); //nRows rows
         c = (int)(Math.random()*nCols); //nCols columns
         Coord cor = new Coord(r,c); 
         while(coordArray.contains(cor))
         {
            r = (int)(Math.random()*nRows);
            c = (int)(Math.random()*nCols); //nCols columns
            cor = new Coord(r,c);
         }  
         System.out.println("x: "+ x);
         coordArray.add(cor);
            
      }
      
      cordSort(coordArray, coordArray.size());
      double time1 = System.nanoTime();
      iterations = new ArrayList<Instancem>(); //iterations
      
 
	  for(int j=0; j<=7; j++) 
      {    	 
    	  for(int x=0; x<nIterations; x++) 
         {
    		  int a=0;
            Double[][][] matrix = new Double[nFrames][nCoordsSize][nCoordsSize]; 
            
            filename= "/home/pi/Documents/BlueJProjects/kNNPi/S1_txt/S1_"+ dataFile + (j+3)+ "_"+ (x+1) + "_f" + (a+1) + ".txt"; //getting textfile //add NewMotions/
            //System.out.println(filename);
            file = new File(filename);
            while(file.exists() && !file.isDirectory()) 
            { 
               System.out.println(filename);
               int c1=0; //col in matrix
               int r1=0; //row
               int numCor=0;
               //Scanner infile = new Scanner(new File(filename)); 
               BufferedReader infile = new BufferedReader(new FileReader(filename));
               for(int z=0; z<nRows; z++) //for every row
               {
                  String s = infile.readLine();
                  String[] cols = s.split("  ");
                     while(numCor<coordArray.size() && coordArray.get(numCor).getRow()==z) //while there are coords dealing with this row
                     {
                    	 
                    	 matrix[a][r1][c1]= Double.parseDouble(cols[coordArray.get(numCor).getCol()+1]);

                           c1++; //column in 3d matrix
                           numCor++;
                           if(c1>=nCoordsSize) //on last col in matrix (which is nCoordsSize by nCoordsSize)
                           {
                              c1=0; //go to the next line in the matrix to store data (column will be zero again)
                              r1++; //increment row
                           }

                     }
                              
               }
               infile.close();
               a++;
               
             filename= "/home/pi/Documents/BlueJProjects/kNNPi/S1_txt/S1_"+ dataFile + (j+3)+ "_"+ (x+1) + "_f" + (a+1) + ".txt";
   
             file = new File(filename); 
            } 
            Instancem I = new Instancem(matrix,j);
            iterations.add(I);


         }
      } 
  
      double time2=System.nanoTime();
      double acTime=time2-time1;
      System.out.println("Acquisition Time" +((double)acTime / 1000000000) );
      for(int u=0; u<30; u++)
      {
      	Collections.swap(iterations, (int)(Math.random()*0+iterations.size()-1), (int)(Math.random()*0+(iterations.size()-1)));
      }
      
      report(iterations);

      //double endTime = System.nanoTime(); // end timer
      //double duration = (endTime - newStart)+timePt1;
      //System.out.println("Runtime: " + duration);
      //final double seconds = ((double)duration / 1000000000);
      //System.out.println("Runtime in seconds:" + seconds);
      System.out.println("correlationTime:" + (double)corrTime / 1000000000/360);
      System.out.println("patternTime:" + (double)patternTime / 1000000000/370);
      System.out.println("classificationTime:" + (double)classTime / 1000000000/10);
      
   }
   public static int[][] validate(ArrayList<Instancem> iterations, int x) 
   {
	  int matrix[][] = new int[8][8];
	   int mod = x%4; //9
      Instancem test = iterations.get(x);
      int c= iterations.get(x).getS(); 
      ArrayList<Instancem> train = new ArrayList<Instancem>(); 
      if(x<=3)
      {
    	  for(int a=4; a<iterations.size(); a++)
	      {

	        	  train.add(iterations.get(a));  
	      }
      }
      else if(mod ==3) //7 don't add 4-7
    {
    	  for(int a=0; a<x-3; a++) //adds 0 to 3
	      {
	        	  train.add(iterations.get(a));  
	      }
    	  for(int a=x+1; x<iterations.size(); x++) // will go from 8 to end
    	  {
	        	  train.add(iterations.get(a));  
    	  } 
    	  
      }
      else if (mod<3 && mod>0) // 6 don't add 4-7
      {
    	  int n = x + (4 - x % 4); //upper bound factor of 4 ==8
    	  for(int a=0; a<n-4; a++) //adds 0 to 3
	      {
	        	  train.add(iterations.get(a));         
	      }
    	  for(int a=n; a<iterations.size(); a++) 
	      {

	        	  train.add(iterations.get(a));         
	      }  
      }
      else if(mod==0) //8 don't add 8-11
      {
    	  for(int a=0; a<x; a++) //adds 0 to 7
	      {
	        	  train.add(iterations.get(a));          
	      }
    	  for(int a=x+4; a<iterations.size(); a++) //adds 12 to nd
	      {
	        	  train.add(iterations.get(a));       
	      }  
      }
  

      int nn = nearestNeighbor(test, train);
      //Print Time out here

      matrix[c][nn]++;
      System.out.println("c: " + c + "nn: " + nn);
                //finish       
	return matrix;
  }
   
   
  public static void report(ArrayList<Instancem> iterations)
   {
      
	  double[] accuracy = new double[10]; 

      ArrayList<ArrayList<Double>> precision = new ArrayList<ArrayList<Double>>();
      ArrayList<ArrayList<Double>> recall = new ArrayList<ArrayList<Double>>();
	  int ks=10; //10 sets each set has 4 instances  
      
	   int e=0;
	   int x=0;
      while(e<iterations.size())
      {
         int r=(int)(Math.random()*e+3);
    	  int matrix[][]= validate(iterations,r); 
    	  freqTested[iterations.get(r).getS()]++;
    	  accuracy[x]=accuracy(matrix);
          precision.add(x,pres(matrix));//x is trial number in pres() there are diff precisions for diff classes
          recall.add(x,recall(matrix));      
         e+=(iterations.size()/ks); 
         x++;
      }

      double acsum=0;
      for(int x1=0; x1<accuracy.length; x1++)
      {
         acsum+=accuracy[x1];
      }
      double totaccuracy=(acsum/accuracy.length)*100;
      System.out.println("Accuracy:"+ totaccuracy);
      double[] sumpres = new double[8]; //sum of precision arrays
      for(int x1=0; x1<ks; x1++)
      {
         ArrayList<Double> section = precision.get(x1);
         for(int y=0; y<section.size(); y++)
         {
            sumpres[y]+=section.get(y);
         }   
      }

      for(int x1=0; x1<sumpres.length; x1++)
      {
         System.out.println("Precision for classification #" + (x1+1) + " : " + (sumpres[x1]/ks));
      }
      double[] sumrecall = new double[4]; //sum of precision arrays
      for(int x1=0; x1<ks; x1++)
      {
         ArrayList<Double> section = recall.get(x1);
         for(int y=0; y<section.size(); y++)
         {
            sumrecall[y]+=section.get(y);
         }   
      }
      System.out.println();
      for(int x1=0; x1<sumrecall.length; x1++)
      {
         System.out.println("Recall for classification #" +(x1+1)+ ": " + (sumrecall[x1]/ks));
      }
      for(int x1=0; x1<freqTested.length; x1++)
      {
    	  System.out.println("Freq of" + x1 + "tested: " + freqTested[x1]);
      }

   }   public static int nearestNeighbor(Instancem test, ArrayList<Instancem> train)
   {
   
       ArrayList<Double> corrs = new ArrayList<Double>();
      ArrayList<Integer> ccs = new ArrayList<Integer>(); //classes
      double time1 = System.nanoTime();
      Double[][] avg1 =test.task3();
      double time2=System.nanoTime();
      patternTime+=(time2-time1);
      for(int x=0; x<train.size(); x++)
      {
    	  double time5 = System.nanoTime();
    	 Double[][] avg2= train.get(x).task3();
    	 double time6=System.nanoTime();
    	 patternTime+=(time6-time5);
         double time3 = System.nanoTime();
         double d = correlation(avg1, avg2);
         double time4=System.nanoTime();
         corrTime+=(time4-time3);
         corrs.add(d);
         ccs.add(train.get(x).getS()); //class corresponding to correlation value of test and train x
      }
      double time9=System.nanoTime();
      sort(corrs,ccs);
      ArrayList<Integer> kccs = new ArrayList<Integer>(); 
      for(int x=0; x<3; x++)
      {
         kccs.add(ccs.get(x));
      }
      int m= mode(kccs);  
      double time10=System.nanoTime();
      classTime+=(time10-time9);
      return m;
   
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
      return Math.abs(top/bottom); 
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

   public static void cordSort(ArrayList<Coord> coordArray, int nCoords)
   {
   	// sorts coordinate array by rows and columns
   	
      Coord temp;
       
       // sort array elements by row
   
      for(int i=0; i<nCoords-1; i++ )
      
         for(int j=0; j<nCoords-1-i; j++)
         
            if( coordArray.get(j).row > coordArray.get(j+1).row ) // if true, swap elements
            {
               temp = coordArray.get(j);
               coordArray.set(j, coordArray.get(j+1));
               coordArray.set(j+1, temp);
            }
   
       // for each row, sort array elements by column
   
      for(int i=0; i<nCoords-1;)
      {
         int currentRow = coordArray.get(i).row;
      
         int nCols=1;
      
         for(int j=i+1; j<nCoords; j++)
         
            if( coordArray.get(j).row == currentRow ) nCols++;
            else 
               break;
      
         for(int j=i, n=0; j < i+nCols-1; j++, n++)
           	
            for(int k=i; k < i+nCols-1-n; k++)
            
               if( coordArray.get(k).col > coordArray.get(k+1).col )  // if true, swap elements
               {
                  temp = coordArray.get(k);
                  coordArray.set(k,coordArray.get(k+1));
                  coordArray.set(k+1, temp);
               } 
           
         i += nCols;
      }
   	
   }

  public static int mode(ArrayList<Integer> arr)
   {
     int freq[] = new int[8]; 
     for(int x=0; x<arr.size(); x++)
      {
         int i = arr.get(x);
         freq[i]++;
      }
    int max=0;
    for(int x=1; x<freq.length; x++)
      {
         if(freq[max]<freq[x])
            {
               max=x;
            }
      }
      return max;         
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
         if(corrs.get(first)>=pivot) //checks to see if its on the right side
         {
            first++;  //like a for loop, it goes to the next cell
         }
         else if(corrs.get(last)<=pivot)
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
