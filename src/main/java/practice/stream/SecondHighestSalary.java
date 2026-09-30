package practice.stream;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SecondHighestSalary {

    public static void main(String[] args) {

        Employee e1 = new Employee(1, 100, "HR");
        Employee e2 = new Employee(2, 200, "IT");
        Employee e3 = new Employee(3, 300, "HR");
        Employee e4 = new Employee(4, 400, "FIN");
        Employee e5 = new Employee(4, 500, "HR");

        List<Employee> employees = List.of(e1, e2, e3, e4, e5);


        highestSalInEachDept(employees);
    }


    public static void secondHighest(List<Employee> employees) {
        Optional<Employee> e = employees.stream().sorted(Comparator.comparingDouble(Employee::salary).reversed()).skip(1).findFirst();
        System.out.println("Second highest employee : " + e.get().id() + " Salary:" + e.get().salary());
    }

    public static void  highestSalInEachDept(List<Employee> employees) {

       Map<String, Optional<Employee>>  map = employees.stream().collect(Collectors.groupingBy(Employee::dept, Collectors.maxBy(Comparator.comparingDouble(Employee::salary))));
       System.out.println("Second highest employee : " + map );

        Map<String, Employee>  map2 = employees.stream().collect(Collectors.groupingBy(Employee::dept, Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingDouble(Employee::salary)), Optional::get)));

        System.out.println("Second highest employee map2 : " + map2 );
    }

}
