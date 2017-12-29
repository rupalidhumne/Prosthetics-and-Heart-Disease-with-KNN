import java.io.*;
import java.util.*;
public class Generic_RupaliDhumne
   {
      public static void main(String[] args) throws Exception
         {
            Scanner kb = new Scanner(System.in);
            System.out.println("Enter filename:");
            String filename = kb.next();
            System.out.println("Enter value of k:");
            int k = kb.nextInt();
            //NearestNeighborClass n = new NearestNeighborClass(filename, k);

            ArrayList<String> attributes = new ArrayList<String>();
             
               kfoldclass kf = new kfoldclass(filename,k);
               kf.report();
              
                       
         }
   }       
            
   