package com.example.sameerwebhook.service;

public final class SQLSolu {
  private SQLSolu() {}

  public static final String Q1_POSTGRES =
      "SELECT p.amount AS salary,\n" +
      "       e.first_name || ' ' || e.last_name AS name,\n" +
      "       CAST(date_part('year', age(current_date, e.dob)) AS int) AS age,\n" +
      "       d.department_name\n" +
      "FROM payments p\n" +
      "JOIN employee e   ON e.emp_id = p.emp_id\n" +
      "JOIN department d ON d.department_id = e.department\n" +
      "WHERE EXTRACT(DAY FROM p.payment_time) <> 1\n" +
      "ORDER BY p.amount DESC\n" +
      "LIMIT 1;";
}
