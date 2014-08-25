Database
======================

The database is solely maintained via [Liquibase] (http://www.liquibase.org/documentation/changes/index.html). 
The liquibase scripts are automatically run when the application starts or integration tests are performed.

Default User
=======================
- User Id: super_admin
- Password: super_admin

Conventions
=========================
All tables should be lowercase named with _ separating logical sections: 
e.g. table_name_for_entity_whatever.

All constraints __must__ have a name:

- Primary Keys: pk_table_name
- Foreign Keys: fk_base_table_name_reference_table_name_column_name
- Unique Key/Index: uk_table_name_column_name
- Index: idx_table_name_column_name