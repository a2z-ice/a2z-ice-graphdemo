package code.practices;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TraversingConcurrentHashMap {

    public static void main(String...args){
        Student [] students = {
                new Student("Assad", 5),
                new Student("Zamal", 10),
                new Student("kabir", 18),
                new Student("nizam", 13)
        };
        System.out.println("-----------before sort--------------------");
        Arrays.stream(students).forEach(student -> System.out.println("name:" + student.name + " age:" + student.age));
        Arrays.sort(students, (student1, student2) ->  student2.age - student1.age  );
        System.out.println("-----------after sort--------------------");
        Arrays.stream(students).forEach(student -> System.out.println("name:" + student.name + " age:" + student.age));


        StudentCmp [] studentCmps = {
                new StudentCmp("Assad", 5),
                new StudentCmp("Zamal", 10),
                new StudentCmp("kabir", 18),
                new StudentCmp("nizam", 13)
        };

        System.out.println("Using comparable_---------------------------");
        Arrays.sort(studentCmps);
        Arrays.stream(studentCmps).forEach(System.out::println);

    }

    public static void testPrimitiveArray()
    {
        int arr [][] = {
                {1,2},
                {3,4,5},
                {6,7,8}
        };
        System.out.println("start***************************");
        final int[] flattenArray = toSingleDimension(arr);
        for(int i = 0; i < flattenArray.length; i++) {
            System.out.println("value of : " + flattenArray[i]);
        }

        final int [] reverseArray = reverseOrder(flattenArray);

        Arrays.stream(reverseArray).forEach(System.out::println);

        System.out.println("done--------------------------");

    }

    public static int calculateTotalElement(int [][] array2d){
        int totalElement = 0;
        for (int i=0; i < array2d.length; i++){
            totalElement+= array2d[i].length;
        }
        return totalElement;
    }

    public static int[] flatten(int [][] array2d){
        return Arrays.stream(array2d).flatMapToInt(Arrays::stream).toArray();
    }
   public static int [] toSingleDimension(int [][] array2d){
        if(array2d == null || array2d.length == 0)  return new int[0];
        int index = 0;
        int [] array = new int[calculateTotalElement(array2d)];
        for(int i=0; i<array2d.length; i++){
            for(int j=0; j<array2d[i].length; j++){
                array[index++] = array2d[i][j];
            }
        }
        return array;
   }
   public static int [] reverseOrder(int [] arr) {
       if(arr == null || arr.length == 0)  return new int[0];

       int [] reverseArray = new int[arr.length];
       for(int i=0; i<arr.length; i++){
           reverseArray[i] = arr[arr.length -1 - i];
       }
       return reverseArray;
   }
   public static int [] reverseOrderByStream(int [] arr){
       if(arr == null || arr.length == 0)  return new int[0];
       final List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());
       Collections.reverse(list);
       return list.stream().mapToInt(Integer::intValue).toArray();
   }

   record Student(String name, int age){}

    static class StudentCmp implements Comparable<StudentCmp> {
        int age;
        String name;

        public StudentCmp(String name, int age){
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "name: " + name + " age:" + age;
        }

        @Override
        public int compareTo(@NotNull StudentCmp o) {
            return Integer.compare(this.age,o.age );
        }
    }
}