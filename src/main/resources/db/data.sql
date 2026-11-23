INSERT INTO disciplina (nome, carga_horaria) VALUES
 ('Matemática', 80), ('Português', 80), ('História', 60),
 ('Geografia', 60), ('Ciências', 60), ('Inglês', 40);

INSERT INTO aluno (matricula, nome, email)
SELECT 'A' || LPAD(CAST(X AS VARCHAR), 6, '0'),
       'Aluno ' || X,
       'aluno' || X || '@escola.com'
FROM SYSTEM_RANGE(1, 5000);

INSERT INTO nota (aluno_id, disciplina_id, bimestre, valor)
SELECT a.id, d.id, b.X, ROUND(RAND() * 10, 1)
FROM aluno a
CROSS JOIN disciplina d
CROSS JOIN SYSTEM_RANGE(1, 4) b;
