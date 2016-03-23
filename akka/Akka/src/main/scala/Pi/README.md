The sample application that we will create is using actors to calculate the value of Pi. Calculating Pi is a CPU intensive operation and we will utilize Akka Actors to write a concurrent solution that scales out to multi-core processors. This sample will be extended in future tutorials to use Akka Remote Actors to scale out on multiple machines in a cluster.

We will be using an algorithm that is called “embarrassingly parallel” which just means that each job is completely isolated and not coupled with any other job. Since this algorithm is so parallelizable it suits the actor model very well.

Here is the formula for the algorithm:

![formula ](http://i.imgur.com/qhXgW9D.png)


This is the [Madhava-Leibniz](https://en.wikipedia.org/wiki/Leibniz_formula_for_%CF%80) formula for calculating pi, and it actually calculates one quarter of pi. So to get pi, you do this equation and multiply the result by four.

In this particular algorithm the master splits the series into chunks which are sent out to each worker actor to be processed. When each worker has processed its chunk it sends a result back to the master which aggregates the total result.