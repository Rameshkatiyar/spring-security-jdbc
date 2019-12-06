# **Setup SpringBoot + Spring Security**
It is used to setup security for web login (**Authentication**) and url access (**Authorization**).

In this project we can use any types of authentication.

**Access Control Type**

Here we can control web security access through three types:

1. In Memory Based Access
2. Role Based Access
3. Group Based Access

##### _**1. Setup In Memory Based Access**_
For this we do not need to create the db schema.

**Step1:** Create a `userdetails.csv` and keep it on a server directory.

Csv contain the following information:
1. username (Example: Jack)
2. password (It should store in hashed form. Example: $2a$15$lHbHDP8sdVRVotSVbAZw9ONdLf0MDQbGr6hKakucqJDjuGhnHQOXW)
3. role (Example: ADMIN or USER)

**Step2:** Update the `security.properties` file with access control type and csv path.

1. access.control.type=IN_MEMORY
2. inMemory.csv.path=/Users/rameshkatiyar/IdeaProjects/app-one/properties/userdetails.csv

##### _**2. Setup Role Based Access**_

**Step1:** Create the schemas `users` and `authorities`.

`create table users(
    username varchar(50) NOT NULL,
    password varchar(250) NOT NULL,
    enabled boolean NOT NULL DEFAULT FALSE,
    primary key(username)
 );`
 
`create table authorities (
   user_role_id SERIAL PRIMARY KEY,
   username varchar(50) NOT NULL,
   authority varchar(40) NOT NULL,
   UNIQUE (username,authority),
   FOREIGN KEY (username) REFERENCES users (username)
 );`

Update both the tables with respective entries.

**Example:**

 - insert into users(username,password,enabled) VALUES ('jack','$2a$15$lHbHDP8sdVRVotSVbAZw9ONdLf0MDQbGr6hKakucqJDjuGhnHQOXW', true);
 - insert into users(username,password,enabled) VALUES ('peter','$2a$15$lHbHDP8sdVRVotSVbAZw9ONdLf0MDQbGr6hKakucqJDjuGhnHQOXW', true);
  
  
 - insert into authorities (username, authority) VALUES ('jack', 'ROLE_USER');
 - insert into authorities (username, authority) VALUES ('jack', 'ROLE_ADMIN');
 - insert into authorities (username, authority) VALUES ('peter', 'ROLE_USER');
 
 **Step2:** Update the `security.properties` file with access control type `ROLE_BASED`.
 
 access.control.type=ROLE_BASED
 
 
 **Step3:** Add the data source properties in property file to connect the db.

 **Example:** For Postgre Database.
 
 - spring.datasource.url=jdbc:postgresql://localhost:5432/testdb
 - spring.datasource.username=rameshkatiyar
 - spring.datasource.password=
 - spring.jpa.generate-ddl=true
 
##### _**3. Setup Group Based Access**_
 
 **Step1:** Create the schemas `groups`, `group_authorities` and `group_members`.
 
`create table groups (
   id bigint generated by default as identity(start with 1) primary 
   key,
   group_name varchar(256) not null
 );`
 
`create table group_authorities (
   group_id bigint not null,
   authority varchar(256) not null,
   constraint fk_group_authorities_group
   foreign key(group_id) references groups(id)
 );`
 
 `create table group_members (
   id bigint generated by default as identity(start with 0) primary 
   key,
   username varchar(256) not null,
   group_id bigint not null,
   constraint fk_group_members_group
   foreign key(group_id) references groups(id)
 );`
 
 Update all the tables with respective entries.
 
 **Example:**

- insert into groups(group_name) values ('Users');
- insert into groups(group_name) values ('Administrators');
 
 
- insert into group_authorities(group_id, authority)
   select id,'ROLE_USER' from groups where group_name='Users';
- insert into group_authorities(group_id, authority)
   select id,'ROLE_USER' from groups where 
   group_name='Administrators';
- insert into group_authorities(group_id, authority)
   select id,'ROLE_ADMIN' from groups where 
   group_name='Administrators';
 
 
- insert into group_members(group_id, username)
   select id,'jack' from groups where 
   group_name='Users';
- insert into group_members(group_id, username)
   select id,'peter' from groups where 
   group_name='Administrators';
   
**Step2:** Update the `security.properties` file with access control type `GROUP_BASED`.
    
access.control.type=GROUP_BASED

**Step3:** Add the data source properties in property file to connect the db.