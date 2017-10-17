- Rename you spip installation folder as spip (version: 2.1.10)
- Execute the patch command:

patch -p0 < spip.patch

patch --binary -p0 < spip.patch (under Windows)

- add empty directory sites under the spip
- add empty directory log under the spip
- make the owner changes for the spip directory that will be the root document for apache server
   (for instance chown -R www-data.www-data spip)
   
- Now you can if you want rename you folder spip as it was originally

- Note sur ls configurations d'apache:
======================================
    
   . Rendre enable le module rewite (a2enmod rewrite)

      
   . Mettre: AllowOverride All   
     sur le root document du site spip.
     ex. extrait du fichier de conf:
     <Directory "/home/blachonm/Spip_html/spip">������ ������ 
          Options Indexes MultiViews FollowSymLinks
          AllowOverride All
          Order allow,deny
          Allow from all
      </Directory> 
      dans le fichier de config d'apache pour le site spip.
      
    
    Autres remarques:
    -----------------
       - si l'application est supprim��e, les tables de site spip sont effac��es mais le sh��ma demeure. Cela n'empeche pas
         de recr��er un autre site avec le meme nom d'application.
         Le r��pertoire des configuration et de fonctionnement sous: sites, est renomm�� avec un sufixe constitu�� de
         la date de l'op��ration (ex.: projet1_spip2.localhost20-01-2012-3:03:50-pm).
         
       - version de SPIP utilis��: 2.1.10
       
start date  [spip.patch]
- Original patch 

16/05/2013 [spip-2.1.10-002.patch]
- allows to display icons for creating arcticles, rubrique, ...

16/05/2013 [spip-2.1.10-003.patch]
- avoid using localhost into for spip site.

18/05/2013 [spip-2.1.10-004.patch]
- use CAS from the forme (patched for the portal)

2/07/2013 [spip-2.1.10-005.patch]
- adding public url to the page preventing visitor role accessing private site (default one for forge portal)

4/07/2013 [spip-2.1.10-006.patch]
- defining new log location to be taken into account by NovaForge logger service.

4/07/2013 [spip-2.1.10-007.patch]
- removing soap/api test code.
