import java.io.File;
import java.util.*;
public class ksimcorr  
{
   
   static ArrayList<Instancem> iterations;
  
   
   // Real Values
   //# files = nFingers * nMotions * nFrames
   static final int nRows = 480;
   static final int nCols= 640;
   
   static final int nCoords=30625;
   static final int nCoordsSize=175;
   
   static final int nActions = 4;
   static final int nIterations = 1;

   static int nFrames; //# frames = # files
   
   static final String dataFile="Motion";
   
   
   public static void main(String[] args) throws Exception
   {
    
      System.out.println("Is running");
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
      Scanner kb = new Scanner(System.in); 
      double startTime = System.nanoTime();
      iterations = new ArrayList<Instancem>(); //iterations
      for(int j=0; j<nActions; j++) 
      {
        
     	if(j==0)
     		nFrames =  17;
     	if(j==1)
     		nFrames = 16;
     	if(j==2)
     		nFrames= 21;
     	if(j==3)
     		nFrames=18;
         	
         int start =0;
         int end=nFrames;
         for(int x=0; x<nIterations; x++) 
         {
            int f=0;
            Double[][][] matrix = new Double[nFrames][nCoordsSize][nCoordsSize]; 
            for(int a=start; a<end; a++) 
            {
                System.out.println("j: "+ j + " a: " + a + "nFrames:" +nFrames);
               String filename= "/Users/rdhumne/Documents/GMUInternship2015/NewMotions/" + dataFile + (j+1) + "_" + (a+1) + ".txt"; //getting textfile
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
                           //System.out.println("f:" +f + " r1: " + r1 + " c1: " +c1);
                        	matrix[f][r1][c1]= infile.nextDouble(); //store the coordinate
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
               f++;
               infile.close();
            } 
             	 	
            Instancem I = new Instancem(matrix,j);
          
            iterations.add(I);
            start+=nFrames;
            end +=nFrames; 
         }
      } 
      kb.close();
      if(iterations!=null)
      {
         report(iterations);
      }
      else
      {
         System.out.println("Got here too fast");
      }
      double endTime = System.nanoTime(); // end timer
      double duration = endTime - startTime;
      System.out.println("Runtime: " + duration);
      final double seconds = ((double)duration / 1000000000);
      System.out.println("Runtime in seconds:" + seconds);
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
      //Print Time out here
      matrix[c][nn]++;
                //finish       
      
      
      return matrix;
   }
   
   
  public static void report(ArrayList<Instancem> iterations)
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
   public static int nearestNeighbor(Instancem test, ArrayList<Instancem> train)
   {
   
       ArrayList<Double> corrs = new ArrayList<Double>();
      ArrayList<Integer> ccs = new ArrayList<Integer>(); //classes
      Double[][] avg1 =test.task3(test.getS());
      for(int x=0; x<train.size(); x++)
      {
         Double[][] avg2= train.get(x).task3(train.get(x).getS());
         double d = correlation(avg1, avg2);
         corrs.add(d);
         System.out.println("correlation value:" + d);
         ccs.add(train.get(x).getS()); //class corresponding to correlation value of test and train x
      }
      sort(corrs,ccs);
      //ArrayList<Double> kcorrs = new ArrayList<Double>();
      ArrayList<Integer> kccs = new ArrayList<Integer>(); 
      for(int x=0; x<3; x++)
      {
         //kcorrs.add(corrs.get(x));
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

