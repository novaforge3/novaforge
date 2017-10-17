insert into nova_histo 

	select now() as dt,
	
    ce.valeurEtat as 'EtatAno',
	
    IFNULL((select COUNT(*) 
	from mantis_bug_table bt,
	 correspondance_severite cs,
	 mantis_project_table p 
	where
	 p.id = bt.project_id
	 and bt.project_id = bt2.project_id
	 and p.rapport = 1 
	 and bt.severity = cs.codeSeverite
	 and bt.status=codeState
	 and cs.valeur = 'bloquante'
	 group by bt.status),0) as blocante,
	
    IFNULL((select COUNT(*) 
	from mantis_bug_table bt,
	 correspondance_severite cs,
	 mantis_project_table p 
	where
	 p.id = bt.project_id
	 and bt.project_id = bt2.project_id
	 and p.rapport = 1 
	 and bt.severity = cs.codeSeverite
	 and bt.status=codeState
	 and cs.valeur = 'majeure'
	group by bt.status),0) as majeure,
	
    IFNULL((select COUNT(*) 
	from mantis_bug_table bt,
	 correspondance_severite cs,
	 mantis_project_table p 
	where
	 p.id = bt.project_id
	 and bt.project_id = bt2.project_id
	 and p.rapport = 1 
	 and bt.severity = cs.codeSeverite
	 and bt.status=codeState
	 and cs.valeur = 'mineure' 
	group by bt.status),0) as mineure,
	
    IFNULL((select COUNT(*) 
	from mantis_bug_table bt,
	 correspondance_severite cs,
	 mantis_project_table p 
	where
	 p.id = bt.project_id
	 and bt.project_id = bt2.project_id
	 and p.rapport = 1 
	 and bt.severity = cs.codeSeverite
	 and bt.status=codeState
	group by bt.status),0) as total,
	
    bt2.project_id as 'bull'
	
    from correspondance_etat ce, mantis_bug_table bt2
	group by bt2.project_id, EtatAno order by EtatAno;
    