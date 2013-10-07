#User schema

#--- !Ups

CREATE SEQUENCE user_id_seq;
CREATE TABLE user (
	id integer NOT NULL DEFAULT nextval('user_id_seq'), 
	firstName varchar(255),
	lastName varchar(255),
	displayName varchar(255),
	email varchar(255)
);

CREATE SEQUENCE answer_id_seq;
CREATE TABLE answer (
	id integer NOT NULL DEFAULT nextval('answer_id_seq'),
	userId integer,
	answer varchar(255) NULL,
	points integer NULL
);

#--- !Downs

DROP TABLE user;
DROP SEQUENCE user_id_seq;

DROP TABLE answer;
DROP SEQUENCE answer_id_seq;