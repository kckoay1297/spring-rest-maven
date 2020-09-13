DROP TABLE IF EXISTS user ;
DROP sequence IF EXISTS user_id_seq ;
create sequence user_id_seq start 1;

create table USER (
	USER_ID bigint auto_increment  not null,
	ACCOUNT_NAME varchar(40) not null,
	ROLE varchar(10) not null,
	STATUS varchar(40) not null, 
	JOIN_DATE date not null
);

insert into user (ACCOUNT_NAME, ROLE, STATUS, JOIN_DATE) values ('John Doe', 'MEMBER','active',CURRENT_TIMESTAMP);
insert into user (ACCOUNT_NAME, ROLE, STATUS, JOIN_DATE) values ('Ali Abu', 'MEMBER','active',CURRENT_TIMESTAMP);
insert into user (ACCOUNT_NAME, ROLE, STATUS, JOIN_DATE) values ('Karen Maria', 'LIBRARIAN','active',CURRENT_TIMESTAMP);

DROP TABLE IF EXISTS book ;
DROP sequence IF EXISTS book_id_seq ;
create sequence book_id_seq start 1;

create table BOOK (
	BOOK_ID bigint auto_increment  not null,
	BOOK_NAME varchar(120) not null,
	TYPE varchar(40) not null,
	STATUS varchar(40) not null,
	DESCRIPTION varchar(255) , 
	CREATED_DATE date not null
);

insert into book (book_name, type, status, description, created_date) values ('World War Z','Fiction','AVAILABLE','Global zomibe pandamic',CURRENT_TIMESTAMP);
insert into book (book_name, type, status, description, created_date) values ('Harry Potter and the Philosopher(s) Stone','Fiction','AVAILABLE','Magical adventure with Harry Potter',CURRENT_TIMESTAMP);
insert into book (book_name, type, status, description, created_date) values ('Unbroken','Non-Fiction','AVAILABLE','WW2 Survivor',CURRENT_TIMESTAMP);
insert into book (book_name, type, status, description, created_date) values ('Me Before You','Romance','AVAILABLE','Love story',CURRENT_TIMESTAMP);
insert into book (book_name, type, status, description, created_date) values ('Be Happy','Motivational','AVAILABLE','Find a way to be happy',CURRENT_TIMESTAMP);
insert into book (book_name, type, status, description, created_date) values ('The Weight of Our Sky','Motivational','AVAILABLE','Find a way to be happy',CURRENT_TIMESTAMP);

DROP TABLE IF EXISTS BOOK_BORROW_RECORD ;
DROP sequence IF EXISTS book_borrow_id_seq ;
create sequence book_borrow_id_seq start 1;

create table BOOK_BORROW_RECORD (
	BOOK_BORROW_ID bigint auto_increment  not null,
	BOOK_ID bigint not null,
	STATUS varchar(40) not null,
	BORROW_DATE date not null, 
	RETURN_DATE date,
	CUSTOMER_ID bigint not null
);

