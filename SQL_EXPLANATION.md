# SQL Query Detailed Explanation

## Problem Statement (Question 2 - Even regNo)

For every department, calculate:
1. Average age (as of 2025-12-01) of employees with ANY payment > ₹70,000
2. Concatenated list of their full names (up to 10)
3. Include only departments with qualifying employees
4. Order by DEPARTMENT_ID DESC

## Sample Data Analysis

### Step 1: Identify Qualifying Employees
Employees with ANY payment > 70,000:

| EMP_ID | Name | Department | DOB | Any Payment > 70k? | Max Payment |
|--------|------|------------|-----|-------------------|-------------|
| 1 | John Williams | Engineering (3) | 1980-05-15 | ✅ Yes | 70,837.00 |
| 2 | Sarah Johnson | Finance (2) | 1990-07-20 | ✅ Yes | 72,984.00 |
| 3 | Michael Smith | Engineering (3) | 1985-02-10 | ✅ Yes | 70,098.00 |
| 4 | Emily Brown | Sales (4) | 1992-11-30 | ✅ Yes | 74,998.00 |
| 5 | David Jones | Marketing (5) | 1988-09-05 | ✅ Yes | 71,475.00 |
| 6 | Olivia Davis | HR (1) | 1995-04-12 | ✅ Yes | 70,198.00 |
| 7 | James Wilson | IT (6) | 1983-03-25 | ❌ No | 69,628.00 |
| 8 | Sophia Anderson | Sales (4) | 1991-08-17 | ❌ No | 62,736.00 |
| 9 | Liam Miller | HR (1) | 1979-12-01 | ❌ No | 69,628.00 |
| 10 | Emma Taylor | Marketing (5) | 1993-06-28 | ❌ No | 69,871.00 |

### Step 2: Calculate Ages (as of 2025-12-01)

For each qualifying employee:

| EMP_ID | Name | DOB | Birth Month/Day | Age Calculation | Final Age |
|--------|------|-----|----------------|-----------------|-----------|
| 1 | John Williams | 1980-05-15 | 05/15 | 2025 - 1980 - 0 | **45** |
| 2 | Sarah Johnson | 1990-07-20 | 07/20 | 2025 - 1990 - 0 | **35** |
| 3 | Michael Smith | 1985-02-10 | 02/10 | 2025 - 1985 - 0 | **40** |
| 4 | Emily Brown | 1992-11-30 | 11/30 | 2025 - 1992 - 0 | **33** |
| 5 | David Jones | 1988-09-05 | 09/05 | 2025 - 1988 - 0 | **37** |
| 6 | Olivia Davis | 1995-04-12 | 04/12 | 2025 - 1995 - 0 | **30** |

**Age Calculation Logic**:
```sql
TIMESTAMPDIFF(YEAR, DOB, '2025-12-01') - 
CASE 
    WHEN DATE_FORMAT(DOB, '%m%d') > DATE_FORMAT('2025-12-01', '%m%d') 
    THEN 1 
    ELSE 0 
END
```

- If birthday hasn't occurred yet this year (month/day > 12/01), subtract 1
- All sample employees have birthdays before December 1st, so no subtraction needed

### Step 3: Group by Department

| Dept ID | Department | Employees | Ages | Average Age |
|---------|-----------|-----------|------|-------------|
| 1 | HR | Olivia Davis | 30 | **30.00** |
| 2 | Finance | Sarah Johnson | 35 | **35.00** |
| 3 | Engineering | John Williams, Michael Smith | 45, 40 | **42.50** |
| 4 | Sales | Emily Brown | 33 | **33.00** |
| 5 | Marketing | David Jones | 37 | **37.00** |
| 6 | IT | *(none)* | - | *(excluded)* |

### Step 4: Final Output (Ordered by DEPARTMENT DESC)

| DEPARTMENT_NAME | AVERAGE_AGE | EMPLOYEE_LIST |
|-----------------|-------------|---------------|
| Marketing | 37.00 | David Jones |
| Sales | 33.00 | Emily Brown |
| Engineering | 42.50 | John Williams, Michael Smith |
| Finance | 35.00 | Sarah Johnson |
| HR | 30.00 | Olivia Davis |

## SQL Query Breakdown

### CTE 1: QualifyingEmployees
```sql
WITH QualifyingEmployees AS (
    SELECT DISTINCT 
        e.EMP_ID,
        e.FIRST_NAME,
        e.LAST_NAME,
        e.DOB,
        e.DEPARTMENT,
        d.DEPARTMENT_NAME
    FROM EMPLOYEE e
    INNER JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
    INNER JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID
    WHERE p.AMOUNT > 70000
)
```
**Purpose**: Filter employees who have **at least one** payment > 70,000
- Uses `DISTINCT` to avoid duplicates (employees may have multiple payments)
- Joins all three tables
- `WHERE p.AMOUNT > 70000` is the critical filter

### CTE 2: EmployeeAges
```sql
EmployeeAges AS (
    SELECT 
        DEPARTMENT,
        DEPARTMENT_NAME,
        EMP_ID,
        CONCAT(FIRST_NAME, ' ', LAST_NAME) AS FULL_NAME,
        TIMESTAMPDIFF(YEAR, DOB, '2025-12-01') - 
        CASE 
            WHEN DATE_FORMAT(DOB, '%m%d') > DATE_FORMAT('2025-12-01', '%m%d') 
            THEN 1 
            ELSE 0 
        END AS AGE,
        ROW_NUMBER() OVER (PARTITION BY DEPARTMENT ORDER BY EMP_ID) AS RN
    FROM QualifyingEmployees
)
```
**Purpose**: Calculate age and assign row numbers
- `CONCAT(FIRST_NAME, ' ', LAST_NAME)` creates full name
- Age calculation accounts for whether birthday has passed
- `ROW_NUMBER()` partitioned by department for limiting to 10 names

### Final SELECT
```sql
SELECT 
    DEPARTMENT_NAME,
    ROUND(AVG(AGE), 2) AS AVERAGE_AGE,
    GROUP_CONCAT(
        CASE WHEN RN <= 10 THEN FULL_NAME END 
        ORDER BY EMP_ID 
        SEPARATOR ', '
    ) AS EMPLOYEE_LIST
FROM EmployeeAges
GROUP BY DEPARTMENT, DEPARTMENT_NAME
ORDER BY DEPARTMENT DESC
```
**Purpose**: Aggregate results
- `AVG(AGE)` calculates average age per department
- `ROUND(..., 2)` rounds to 2 decimal places
- `GROUP_CONCAT` with `CASE WHEN RN <= 10` limits to first 10 employees
- `ORDER BY DEPARTMENT DESC` sorts by department ID descending

## Edge Cases Handled

1. **Employees with multiple payments > 70k**: `DISTINCT` in first CTE ensures no duplicates
2. **Exact 70,000 threshold**: Uses `> 70000` (strictly greater than)
3. **More than 10 employees in a department**: `ROW_NUMBER()` with `CASE WHEN RN <= 10` limits
4. **Empty departments**: Automatically excluded by `INNER JOIN` and filtering
5. **Age calculation precision**: Accounts for whether birthday has occurred this year

## Database Compatibility

This query is written for **MySQL** but can be adapted:

### For PostgreSQL:
Replace:
- `GROUP_CONCAT(... SEPARATOR ', ')` with `STRING_AGG(..., ', ')`
- `DATE_FORMAT(DOB, '%m%d')` with `TO_CHAR(DOB, 'MMDD')`

### For SQL Server:
Replace:
- `GROUP_CONCAT(...)` with `STRING_AGG(..., ', ')`
- `TIMESTAMPDIFF(YEAR, DOB, '2025-12-01')` with `DATEDIFF(YEAR, DOB, '2025-12-01')`

## Verification Steps

To verify the query:
1. Run against test data
2. Check that 6 qualifying employees exist
3. Verify ages are calculated correctly
4. Confirm IT department is excluded (no employees > 70k)
5. Validate ordering (Marketing → Sales → Engineering → Finance → HR)
6. Ensure name concatenation works with limit

## Performance Considerations

- Uses indexed columns (EMP_ID, DEPARTMENT_ID) for joins
- CTEs improve readability and can be optimized by query planner
- `DISTINCT` in first CTE necessary but may impact performance on large datasets
- Window function `ROW_NUMBER()` is efficient for limiting results

---

**This query is embedded in the Java application and will be submitted automatically on startup.**
