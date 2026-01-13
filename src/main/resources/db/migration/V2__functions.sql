CREATE OR REPLACE FUNCTION fn_project_task_count(p_project_id BIGINT)
RETURNS BIGINT AS $$
  SELECT COUNT(*) FROM tasks WHERE project_id = p_project_id;
$$ LANGUAGE sql STABLE;