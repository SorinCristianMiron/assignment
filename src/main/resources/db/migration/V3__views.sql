CREATE OR REPLACE VIEW v_project_task_stats AS
SELECT
    p.id AS project_id,
    p.name AS project_name,
    COUNT(t.id) AS task_count,
    COUNT(*) FILTER (WHERE t.status = 'DONE') AS done_count
FROM projects p
         LEFT JOIN tasks t ON t.project_id = p.id
GROUP BY p.id, p.name;
