#!/bin/bash

if [ ! -z "$1" ] && [ ! -z "$2" ]
then
{
	export BIRT_HOME=SED_BIRT_HOME
	export PATH=$PATH:@NOVA_HOME/engines/java/jdk1.6.0_26/bin/
	NOVAFORGE_REPORT_HOME=SED_NOVAFORGE_REPORT_HOME
	WWW_HOME=SED_WWW_HOME
	PROJECT_ID=$1
	DATE=$2

	${BIRT_HOME}/ReportEngine/genReport.sh --mode runrender --parameter mantisPrjIds="${PROJECT_ID}" --output ${WWW_HOME}/birt/anomalies_${PROJECT_ID}_${DATE}.doc --format DOC --file ${NOVAFORGE_REPORT_HOME}/report/anomaliesParam.rptdesign

	${BIRT_HOME}/ReportEngine/genReport.sh --mode runrender --parameter mantisPrjIds="${PROJECT_ID}" --output ${WWW_HOME}/birt/anomalies_${PROJECT_ID}_${DATE}.pdf --format PDF --file ${NOVAFORGE_REPORT_HOME}/report/anomaliesParam.rptdesign
	exit 0
}
else 
{
	echo 'Un des paramètres est manquant...'
	echo 'Paramètre 1 : id du projet Mantis'
	echo 'Paramètre 2 : date actuelle au format AAAAMMJJhhmm'
	exit 1
}
fi
