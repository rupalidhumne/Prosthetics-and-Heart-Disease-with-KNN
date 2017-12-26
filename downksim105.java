import java.io.File;
import java.util.*;
public class downksim105
{
   
   static ArrayList<Instancem> iterations;
  
   static final int nRows = 480;
   static final int nCols= 640;
   
   static final int nCoords=30625;
   static final int nCoordsSize=175;
   
   static final int nSubjects = 6;
   static final int nActions = 4;
   static final int nIterations =5 ;

   static int nFrames=28; //# frames = # files
   
   static final String dataFile="Motion";
   static final String sString= "S";
   
   static String filename;
   static File file;
   
   static int valMatrix[][]=new int[13][13]; 
   
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
         coordArray.add(cor);
            
      }
      cordSort(coordArray, coordArray.size());
      
      double startTime = System.nanoTime();
      iterations = new ArrayList<Instancem>(); //iterations
      
 
	  for(int j=0; j<=12; j++) //6 10 12 15
      {    	 

    	  for(int x=0; x<nIterations; x++) 
         {
            
            Double[][][] matrix = new Double[nFrames][nCoordsSize/10][nCoordsSize/10]; 
            int a=0;
            filename= "/Users/rdhumne/Documents/GMUInternship2015/NewMotions/S1_txt/S1_"+ dataFile + (j+3)+ "_"+ (x+1) + "_f" + (a+1) + ".txt"; //getting textfile //add NewMotions/
            file = new File(filename);
            while(file.exists() && !file.isDirectory()) 
            { 
               double[][] frame = new double[480][640];
            	System.out.println(filename);
               int c1=0; //col in matrix
               int r1=0; //row
               int numCor=0;
               Scanner infile = new Scanner(new File(filename)); 
               for(int z=0; z<nRows; z++) //for every row
               {
                  for(int b=0; b<nCols; b++) //for every column
                  {
                     if(numCor<coordArray.size()) //if still have coordinates left to look for
                     {
                        if(coordArray.get(numCor).getRow()==z && coordArray.get(numCor).getCol()==b) //if at a coordinate that is in the coordArray (randomly generated coordinates before)
                        {                                                    
                           frame[r1][c1]= infile.nextDouble(); //store the coordinate
                           c1++; //column in 3d matrix
                           numCor++;
                           if(c1>=nCoordsSize) //on last col in matrix (which is nCoordsSize by nCoordsSize)
                           {
                              c1=0; //go to the next line in the matrix to store data (column will be zero again)
                              r1++; //increment row
                           }
                        }
                        else //if coordinate not in coordArray
                        {
                           infile.nextDouble(); //continue
                        }
                     }
                  }               
               }
               infile.close();
           
               for(int i=0; i<48; i++)
               {
            	   for(int j1=0; j1<64; j1++)
            	   {
            		   matrix[a][i][j]=frame[(i*64)/640][(j1*48)/480];
            	   }
               }
               a++;
               filename= "/Users/rdhumne/Documents/GMUInternship2015/NewMotions/S1_txt/S1_"+ dataFile + (j+3)+ "_"+ (x+1) + "_f" + (a+1) + ".txt";  //getting textfile //add NewMotions/
   
             file = new File(filename); 
            } 
            Instancem I = new Instancem(matrix,j);
            iterations.add(I);


         }
      } 
  
      double startTimept2=System.nanoTime();
      double timePt1=startTimept2-startTime;
      for(int u=0; u<12; u++)
      {
      	Collections.swap(iterations, (int)(Math.random()*0+iterations.size()-1), (int)(Math.random()*0+(iterations.size()-1)));
      }
      double newStart = System.nanoTime();
      report(iterations);

      double endTime = System.nanoTime(); // end timer
      double duration = (endTime - newStart)+timePt1;
      System.out.println("Runtime: " + duration);
      final double seconds = ((double)duration / 1000000000);
      System.out.println("Runtime in seconds:" + seconds);
   }
   public static void validate(ArrayList<Instancem> iterations, int x) 
   {
	  int mod = x%10;
      Instancem test = iterations.get(x);
      int c= iterations.get(x).getS(); 
      ArrayList<Instancem> train = new ArrayList<Instancem>(); 
      if(x<=9)
      {
    	  for(int a=10; a<iterations.size(); a++)
	      {

	        	  train.add(iterations.get(a));  
	      }
      }
      else if(x%10 ==9) // 19 don't take 10-19    
    {
    	  for(int a=0; a<x-9; a++) //will go from 0 to 9 inc
	      {
	        	  train.add(iterations.get(a));  
	      }
    	  for(int a=x+1; x<iterations.size(); x++) // will go from 20 to end
    	  {
    		  if(a!=x)
	          {
	        	  train.add(iterations.get(a));  
	          }
    	  } 
    	  
      }
      else if (mod<9 && mod>0) // 13 10-19 don't take
      {
    	  int n = x + (10 - x % 10); //upper bound -->20
    	  for(int a=0; a<n-10; a++) //will go from 0 to 9 inc
	      {
	        	  train.add(iterations.get(a));         
	      }
    	  for(int a=n; a<iterations.size(); a++) //20 to end
	      {

	        	  train.add(iterations.get(a));         
	      }  
      }
      else if(mod==0) 
      {
    	  for(int a=0; a<x; a++) 
	      {
	        	  train.add(iterations.get(a));          
	      }
    	  for(int a=x+10; a<iterations.size(); a++) 
	      {
	        	  train.add(iterations.get(a));       
	      }  
      }
  

      int nn = nearestNeighbor(test, train);
      //Print Time out here

      valMatrix[c][nn]++;
                //finish       
   }
   
   
  public static void report(ArrayList<Instancem> iterations)
   {
      
      int ks=13; //12 sets each set has 10 instances
      
	   int e=0;
      while(e<iterations.size())
      {
         validate(iterations,(int)(Math.random()*e+4)); //goes 10 up so from 0 -->9
         e+=(iterations.size()/ks); //1-10,  
      }
     // precision.add(e,pres(valMatrix));
      //recall.add(e,recall(valMatrix)); 
     
     
      //for(int x=0; x<accuracy.length; x++)
      //{
         //acsum+=accuracy[x];
      //}
      double totaccuracy=(accuracy(valMatrix)/ks)*100;
      System.out.println("Accuracy:"+ totaccuracy);
      for(int p=0; p<valMatrix.length; p++) //tp/(tp+fp)
      {
    	  System.out.println("Precision for classification #" + (p+1) + " : " + pres(valMatrix,p)*100);
      }
      for(int r=0; r<valMatrix.length; r++) //tp/(tp+fp)
      {
    	  System.out.println("Recall for classification #" + (r+1) + " : " + recall(valMatrix,r)*100);
      }

   }   public static int nearestNeighbor(Instancem test, ArrayList<Instancem> train)
   {
   
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
      ArrayList<Integer> kccs = new ArrayList<Integer>(); 
      for(int x=0; x<5; x++)
      {
         kccs.add(ccs.get(x));
      }
      int m= mode(kccs);  
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
     int freq[] = new int[4]; 
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

   private static double pres(int[][] valMatrix2, int p) 
   {
	      	double pres;

	         double tp = valMatrix[p][p];
	         double fp = fp(valMatrix,p);
	         if(tp==0 && fp==0)
	         {
	            pres= 100.0;
	         }
	         else if(tp==1&& fp==0)
	         {
	            pres= 100.0;
	         }
	         else
	            pres = (tp/(fp+tp))*100;
	            

	   return pres;
   }

   private static double recall(int[][] valMatrix2, int r)
   {          
	   double rec;

       double tp = valMatrix[r][r];
       double fn = fn(valMatrix,r);
       if(tp==0 && fn==0)
       {
          rec= 100.0;
       }
       else if(tp==1&& fn==0)
       {
          rec= 100.0;
       }
       else
          rec = (tp/(fn+tp))*100;
          

 return rec;
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


