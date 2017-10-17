grammar Requirements;

options
{
  // antlr will generate java lexer and parser
  language = Java;
}

// ***************** lexer rules:
//the grammar must contain at least one lexer rule
WS                    : (' '|'\t'|'\n')+ {skip();} ;
VERSION               : ('0'..'9')+;
REQUIREMENT_ID        : ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')+ ;
requirementIdList     : '{' WS? REQUIREMENT_ID WS? (',' WS REQUIREMENT_ID)* WS? '}';
requirementId         : REQUIREMENT_ID;
requirementVersion    : VERSION;
requirementIdDef      : ('id' WS? '=') WS? requirementId;
requirementVersionDef : ('version' WS? '=') WS? requirementVersion;

// ***************** parser rules:
//our grammar accepts only salutation followed by an end symbol
requirement : WS? '@Requirement' WS? '(' WS? requirementIdDef WS? ( ',' WS? requirementVersionDef WS?)?  ')' WS?;
