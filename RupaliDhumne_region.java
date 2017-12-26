import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
public class RupaliDhumne_region
{
   
   static ArrayList<Instancem> iterations;
  
   static final int nRows = 480;
   static final int nCols= 640;
   
   static final int nCoords=30625;
   static final int nCoordsSize=175;
   
   //static final int nSubjects = 6;
   static final int nActions = 11;
   static final int nIterations =5 ;

   static int nFrames=28; //# frames = # files
   
   static final String dataFile="Motion";
   static final String sString= "S";
   
   static String filename;
   static File file;

   static double corrTime, patternTime;
   static int valMatrix[][] = new int[nActions][nActions];
   
   public static void main(String[] args) throws Exception //if still OOM subtract as you bring it in
   {
      ArrayList<Coord> coordArray = new ArrayList<Coord>();
      int r=0; 
      int c=0;
      for(int x=0; x<nCoords; x++) //generating nCoords coordinates
      {
         r = (int)(Math.random()*200+140); //nRows rows
         c = (int)(Math.random()*200+220); //nCols columns 170
         Coord cor = new Coord(r,c); 
         while(coordArray.contains(cor))
         {
        	    r = (int)(Math.random()*200+140); //nRows rows 90
             c = (int)(Math.random()*200+220);
            cor = new Coord(r,c);
         }  
         coordArray.add(cor);
       System.out.println("x:" +x);  
      }
      cordSort(coordArray, coordArray.size());
      
      double time1 = System.nanoTime();
      iterations = new ArrayList<Instancem>(); //iterations
 
	  for(int j=0; j<=nActions-1; j++) 
      {    	 
    	  
    	  for(int x=0; x<nIterations; x++) 
         {
             System.out.println("Reached");

    		      int a=0;
              Double[][][] matrix = new Double[nFrames][nCoordsSize][nCoordsSize]; 
             
              
              filename= "/home/pi/Documents/BlueJProjects/kNNPi/S1_txt/S1_"+ dataFile + (j+3)+ "_"+ (x+1) + "_f" + (a+1) + ".txt"; //getting textfile //add NewMotions/
             // filename= "/Users/rdhumne/Documents/GMUInternship2015/S1_txt/S1_"+ dataFile + (j+3)+ "_"+ (x+1) + "_f" + (a+1) + ".txt";
              System.out.println(filename);
              file = new File(filename);
            while(file.exists() && !file.isDirectory()) 
            { 
               
               System.out.println(filename);
               int c1=0; //col in matrix
               int r1=0; //row
               int numCor=0;
               BufferedReader infile = new BufferedReader(new FileReader(filename));

               for(int l=1; l<140; l++) //skip 89 lines
               {
            	 infile.readLine();
               }
               for(int z=140; z<340; z++) //for every row
               {
            	  String s = infile.readLine();
                  String[] cols = new String[nCols+1];
                  int pos=0, end, i=0;
                  while((end=s.indexOf("   ", pos))>=0)
                  {
                	  if(i==0)
                     cols[i]=s.substring(pos, end);
                    else
                     cols[i]=s.substring(pos+2, end);
                	  pos=end+1;
                    i++;
                  }
                  cols[i]=s.substring(pos+2, s.length());
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
              filename= "/home/pi/Documents/BlueJProjects/kNNPi/S1_txt/S1_"+ dataFile + (j+3)+ "_"+ (x+1) + "_f" + (a+1) + ".txt";  //getting textfile //add NewMotions/
               //filename="/Users/rdhumne/Documents/GMUInternship2015/S1_txt/S1_" + dataFile + (j+3)+ "_"+ (x+1) + "_f" + (a+1) + ".txt";
             file = new File(filename); 
            } 
            Instancem I = new Instancem(matrix,j);
            iterations.add(I);


         }
      } 
  
	  double time2=System.nanoTime();
      double acTime=time2-time1;
      System.out.println("Acquisition Time" +((double)acTime / 1000000000) );

      report(iterations);

      //double endTime = System.nanoTime(); // end timer
      //double duration = (endTime - newStart)+timePt1;
      //System.out.println("Runtime: " + duration);
      //final double seconds = ((double)duration / 1000000000);
      //System.out.println("Runtime in seconds:" + seconds);
      System.out.println("correlationTime:" + (double)corrTime / 1000000000/(54*55)); //40 test instances are each correlated to their 39 other 39 test instances
      System.out.println("patternTime:" + (double)patternTime / 1000000000/(55*55)); //1 activity patern is the test, 39 activity patterns are the train, this happens 40 tims
      //System.out.println("classificationTime:" + (double)classTime / 1000000000/40);
   }

   
  public static void report(ArrayList<Instancem> iterations)
   {
     
      for(int x=0; x<iterations.size(); x++)
      {
    	  int c= iterations.get(x).getS(); 
    	  int nn = nearestNeighbor(x,iterations);
    	  valMatrix[c][nn]++;
    	  System.out.println("c: " + c + "nn: " + nn);
           
      }
     
      double totaccuracy= accuracy(valMatrix);     
      System.out.println("Accuracy:"+ totaccuracy*100);

      for(int x=0; x<nActions; x++)
      {
    	  System.out.println("Precision for classification #" + (x+1) + " : " + pres(x));
      }
      
      for(int x=0; x<nActions; x++)
      {
         System.out.println("Recall for classification #" +(x+1)+ ": " + recall(x));
      }
   }   public static int nearestNeighbor(int test, ArrayList<Instancem> train)
    {
   
       ArrayList<Double> corrs = new ArrayList<Double>();
      ArrayList<Integer> ccs = new ArrayList<Integer>(); //classes
      double time1 = System.nanoTime();
      Double[][] avg1 =iterations.get(test).task3();
      double time2=System.nanoTime();
      patternTime+=(time2-time1);
      for(int x=0; x<train.size(); x++)
      {
          if(x!=test)
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
      }
      //double time9=System.nanoTime();
      sort(corrs,ccs);
      //ArrayList<Integer> kccs = new ArrayList<Integer>(); 
      
      return ccs.get(0);
   
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
   public static double accuracy(int[][]matrix)//only 1 test instance per thing
   {
      
      double tp = tp(matrix); //sum max of 55
      return (tp/(nActions*nIterations));
 
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
     int freq[] = new int[nActions]; 
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

   public static double pres(int action)
   {
      double colsum = 0.0;
      for(int r=0; r<valMatrix.length; r++)
      {
    	  colsum+=valMatrix[r][action];
      }
	   double p = valMatrix[action][action]/colsum;
	   
      return p;    
   }

   public static double recall(int action)
   {
	   double rowsum = 0.0;
	      for(int c=0; c<valMatrix.length; c++)
	      {
	    	  rowsum+=valMatrix[action][c];
	      }
		   double p = valMatrix[action][action]/rowsum;
		   
	      return p;    
   }



      
   public static double fp(int[][]matrix,int y) //sum COLUMN cnn 0
   {
      double sum =0.0;
      //System.out.println(matrix.length);
      for(int x=0; x<matrix.length; x++) //traversing rows inthe column
      {
         if(x!=y)
         {
            sum+=matrix[x][y];
         }
      }
      return sum;
   }                  
   public static double fn(int matrix[][], int x) //sum ROW cnn 0
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
