SET 2 - JAVA STREAMS: 35 QUESTIONS

FOCUS: Streams coding + conceptual questions, with complexity, null handling, duplicate-key handling and readability.

1. Second-highest salary

Coding Questions

employees.stream()
.map(Employee::getSalary)
.distinct()
.sorted(Comparator.reverseOrder())
.skip(1)
.findFirst();

Handle fewer than two distinct salaries.

2. Highest salary in each department

Map<String, Optional<Employee>> result =
employees.stream().collect(Collectors.groupingBy(
Employee::getDepartment,
Collectors.maxBy(Comparator.comparing(Employee::getSalary))
));

3. Duplicate elements

Map<Integer, Long> counts = numbers.stream()
.collect(Collectors.groupingBy(
Function.identity(), Collectors.counting()));

List<Integer> duplicates = counts.entrySet().stream()
.filter(e -> e.getValue() > 1)
.map(Map.Entry::getKey)
.toList();

4. First non-repeated character

Character result = input.chars()
.mapToObj(c -> (char)c)
.collect(Collectors.groupingBy(
Function.identity(), LinkedHashMap::new, Collectors.counting()))
.entrySet().stream()
.filter(e -> e.getValue() == 1)
.map(Map.Entry::getKey)
.findFirst()
.orElse(null);

5. Frequency of each character

Map<Character, Long> freq = input.chars()
.mapToObj(c -> (char)c)
.collect(Collectors.groupingBy(
Function.identity(), Collectors.counting()));

6. Frequency of each word

Map<String, Long> freq = Arrays.stream(sentence.split("\\s+"))
.collect(Collectors.groupingBy(
Function.identity(), Collectors.counting()));

7. First repeated character

Use insertion order plus counts, or maintain a Set in an imperative solution. In an interview, prefer clarity over forcing a stateful Stream.

8. Employees earning more than average

double avg = employees.stream()
.mapToDouble(Employee::getSalary)
.average().orElse(0);

List<Employee> result = employees.stream()
.filter(e -> e.getSalary() > avg)
.toList();

9. Group employees by department

Map<String, List<Employee>> result =
employees.stream().collect(
Collectors.groupingBy(Employee::getDepartment));

10. Sort employees by salary

employees.stream()
.sorted(Comparator.comparing(Employee::getSalary))
.toList();

11. Maximum/minimum

numbers.stream().max(Integer::compareTo);
numbers.stream().min(Integer::compareTo);

12. Top 3 highest salaries

employees.stream()
.map(Employee::getSalary)
.distinct()
.sorted(Comparator.reverseOrder())
.limit(3)
.toList();

13. Nth highest salary

int n = 3;
Optional<Double> salary = employees.stream()
.map(Employee::getSalary)
.distinct()
.sorted(Comparator.reverseOrder())
.skip(n - 1L)
.findFirst();

14. Partition even and odd

Map<Boolean, List<Integer>> result =
numbers.stream().collect(
Collectors.partitioningBy(n -> n % 2 == 0));

15. Sum/average salaries

double sum = employees.stream()
.mapToDouble(Employee::getSalary).sum();

double avg = employees.stream()
.mapToDouble(Employee::getSalary).average().orElse(0);

16. List to Map<Id, Employee>

Map<Long, Employee> map = employees.stream()
.collect(Collectors.toMap(
Employee::getId,
Function.identity(),
(existing, duplicate) -> existing));

Important: decide explicitly what duplicate IDs mean.

17. Names starting with a character

employees.stream()
.filter(e -> e.getName() != null &&
e.getName().startsWith("A"))
.toList();

18. Longest string

strings.stream()
.max(Comparator.comparingInt(String::length));

19. Remove duplicates

numbers.stream().distinct().toList();

20. Common elements between two lists

For large lookup lists:

Set<Integer> lookup = new HashSet<>(list2);
List<Integer> common = list1.stream()
.filter(lookup::contains)
.distinct()
.toList();

This avoids repeatedly scanning list2.