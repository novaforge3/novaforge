use %DATABASE%; 

ALTER TABLE mantis_risk_criteria_table DROP INDEX idx_label ;
ALTER TABLE mantis_risk_criteria_table ADD  UNIQUE INDEX idx_label  (label, project_id);
