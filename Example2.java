
    //import static spark.Spark.*;
        import org.apache.spark.SparkConf;
    //    import org.apache.spark.SparkContext;
    import org.apache.spark.api.java.JavaPairRDD;
    import org.apache.spark.api.java.JavaRDD;
    import org.apache.spark.api.java.JavaSparkContext;
    import org.apache.spark.api.java.function.FlatMapFunction;
    import org.apache.spark.api.java.function.Function2;
    import org.apache.spark.api.java.function.PairFunction;
    import scala.Tuple2;

    import java.util.Arrays;
    //import java.util.Iterator;
    import java.util.List;



    public class Example2 {
            public static void main(String[] args) {

                //get("/hello", (req, res) -> "Hello World");

                SparkConf conf = new SparkConf().setAppName("WordCount").setMaster("local");
                JavaSparkContext sc = new JavaSparkContext(conf);


                JavaRDD lines = sc.textFile("/home/projectone/spark/input/wc1.txt");

                JavaRDD words = lines.flatMap(new FlatMapFunction<String, String>() {
                    public Iterable call(String s) throws Exception {
                        String[] cells = s.split(" ");
                        return Arrays.asList(cells);
                    }
                });

                JavaPairRDD pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
                   public Tuple2<String, Integer> call(String s) throws Exception {
                       return new Tuple2<String, Integer>(s,1);
                   }
                });

                JavaPairRDD wordcounts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
                    public Integer call(Integer a, Integer b) throws Exception {
                        return a.intValue() + b.intValue();
                    }
                });


                List<Tuple2<String, Integer>> localValues = wordcounts.take(10);

                for(Tuple2<String, Integer> t : localValues) {
                    System.out.println(t._1() + ":" + t._2());
                }

            }
        }