import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;

public class FibRecurDP {

    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

    static String ResultsFolderPath = "/home/cody/Results/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;

    static int numberOfTrials = 100;



    public static void main( String[] args)
    {
        //calling full experiment with text file names, running the three experiments as usual for consistency
        runFullExperiment("FibRecurDP-Exp1-ThrowAway.txt");
        runFullExperiment("FibRecurDP-Exp2.txt");
        runFullExperiment("FibRecurDP-Exp3.txt");

        //My testing code used testing the fullest amount in the command bar
       /* int x = 92;
        long result;
        for(int i = 0; i < x; i ++)
        {
            result = fibonacciFunctionCall(i);
            System.out.println(result);
        }
        */
    }
    // modified timing code given to us, changed so that input sizes were reduced and went one at a time
    static void runFullExperiment(String resultsFileName){


        long result = 0;
        long inputsize = 1;
        try {

            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);

        } catch(Exception e) {

            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+resultsFileName);
            return; // not very foolproof... but we do expect to be able to create/open the file...

        }



        ThreadCpuStopWatch BatchStopwatch = new ThreadCpuStopWatch(); // for timing an entire set of trials
        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial


        resultsWriter.println("#InputSize    AverageTime      Fib(x)returned result       x input "); // # marks a comment in gnuplot data
        resultsWriter.flush();
        //uses the x instead of the input
        for(int i = 0 ; i < 92; i++) {

            long batchElapsedTime = 0;


            System.gc();




            // instead of timing each individual trial, we will time the entire set of trials (for a given input size)
            // and divide by the number of trials -- this reduces the impact of the amount of time it takes to call the
            // stopwatch methods themselves
            BatchStopwatch.start(); // comment this line if timing trials individually


            // run the tirals
            for (long trial = 0; trial < numberOfTrials; trial++)
            {

                //call to the fibonaccifuntion gave more descriptive name than fib
                result = fibonacciFunctionCall(i);



            }

            batchElapsedTime = BatchStopwatch.elapsedTime(); // *** comment this line if timing trials individually
            double averageTimePerTrialInBatch = (double) batchElapsedTime / (double)numberOfTrials; // calculate the average time per trial in this batch

            // tying to get doubling ratio to work.
            //double doublingRatio = (double) averageTimePerTrialInBatch / (double) prevTimePerTrial;
            // prevTimePerTrial = averageTimePerTrialInBatch;


            /* print data for this size of input average time and fibonacci result*/
            if(i > 0) {
                inputsize = (long) (Math.log(i) / Math.log(2) + 1);
            }
            resultsWriter.printf("%12d  %15.2f  %20d %15d\n",inputsize, averageTimePerTrialInBatch, result,i );
            // might as well make the columns look nice
            resultsWriter.flush();
            System.out.println(" ....done.");

        }

    }

    // wrapper function used to call the the recursive function
    public static long fibonacciFunctionCall(int x) {
        long result;

        //makes the cache that will be holding the results and setting all to a negative 1
        // so there isn't a need to make another cache
        long[] fibResultsAvailaible = new long [x+1];
        Arrays.fill(fibResultsAvailaible, -1);
        result = fibRecursiveWithCache(x, fibResultsAvailaible);
        return result;


    }
    //The recursive function where most of the recursion will take place
    public static long fibRecursiveWithCache(int x, long cache[])
    {

        long result;
        //used to catch the simple numbers in fibonacci
        if (x < 2)
        {
            return 1;
        }

        //if the cache has a number in it no need to look any further for the result already have it
        else if(cache[x] > -1)
        {

            return cache[x];
        }
        //The usual recursive call plus putting the result of the call into the cache for future use
        //Making this the underlining part of the Dynamic
        else
        {

            result = fibRecursiveWithCache(x-1,cache)+fibRecursiveWithCache(x-2,cache);
            cache[x] = result;
            return result;
        }
    }
}