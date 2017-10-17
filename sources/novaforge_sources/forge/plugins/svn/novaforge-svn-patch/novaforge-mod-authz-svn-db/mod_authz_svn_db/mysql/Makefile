CC = gcc
LDFLAGS = -L/usr/local/lib/mysql -lmysqlclient
CCFLAGS = -g -Wall -O0
INCLUDEDIR = -I/usr/local/include/apache22 -I/usr/local/include/subversion-1 -I/usr/local/include/apr-1 -I.. -I/usr/local/include
RM = -rm


.PHONY: all clean

all: mod_authz_svn_db_mysql.so

mod_authz_svn_db_mysql.so: mod_authz_svn_db_mysql.c ../mod_authz_svn_db.c ../mod_authz_svn_db.h

	$(CC) $(CCFLAGS) $(INCLUDEDIR) -o $@ mod_authz_svn_db_mysql.c $(LDFLAGS) -shared

clean:
	$(RM) *.so

