# Calculate Halstead Complexity Measures

This project is intended to calculate the Halstead complexity metrics (https://en.wikipedia.org/wiki/Halstead_complexity_measures)


Halstead complexity measures are software metrics introduced by Maurice Howard Halstead in 1977. These metrics are computed statically, without program execution.

##Calculation

First we need to compute the following numbers, given the program source code:

n1 = the number of distinct operators
n2 = the number of distinct operands
N1 = the total number of operators
N2 = the total number of operands

From these numbers, five measures can be calculated:
Program vocabulary: n = n1 + n2 \,
Program length: N = N1 + N2  \,
Calculated program length: N'=n1log2n1+n2log2n2
Volume: V= N \times \log_2 n   \,
Difficulty : D= { n_1 \over 2  } \times { N_2 \over n_2 } \,
Effort: E= D * V \,




## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisities

What things you need to install the software and how to install them

```
Give examples
```

### Installing


Clone the project to your local repository:
```
git clone https://ametwally@bitbucket.org/ametwally/hw1_ahmedmetwally.git
```


Navigate to the project's main directory. Then build the project using gradle 
```
gradle build
```


To execute the program:
```
gradle execute
```



Example: 
Download the Protein Family Alignment Annotation Tool (PFAAT) from sourcefodge (https://sourceforge.net/projects/pfaat/?source=typ_redirect)
```
wget https://sourceforge.net/projects/pfaat/files/OldFiles/pfaat-1_0-src.zip
```

Decompress the zipped file:
```
unzip pfaat-1_0-src.zip
```

Execute the CalcHalsteadMetrics program by:
```
build execute
```


Then give the path of the unzipped PFAAT directory 




## Running the tests

There are couple of tests implemented in this program. to test the program using the JUnit tests:

```
gradle test
```




### Implementation Details:


I considered the 38 operators only in my implementation:
=   >   <   !   ~   ?   :   ->
==  >=  <=  !=  &&  ||  ++  --
+   -   *   /   &   |   ^   %   <<   >>   >>>
+=  -=  *=  /=  &=  |=  ^=  %=  <<=  >>=  >>>=

* /// Test public, non static, protected.
* /// LambdaExpression is added in JLS8 API
* /// catch enhanced for and if condition for the : 
* // catch switch for the :
* /// negative numbers counted as 
* /// the program count import statements as Operands
* /// Should we add the operators in different scopes as two different things
* // Count integer lateral numbers only only	
* // Check if the parameters are set or not before calculating any metric
* // make the original parameters valid for division
since it is a reserved keyword from source level 5.0






## Authors

**Ahmed Metwally** 

