# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "USERS" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"FULL_NAME" VARCHAR(254) NOT NULL,"WAY_ID" BIGINT NOT NULL);
create table "WAYS" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"line" geometry NOT NULL);
alter table "USERS" add constraint "USER_WAY_FK" foreign key("WAY_ID") references "WAYS"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "USERS" drop constraint "USER_WAY_FK";
drop table "WAYS";
drop table "USERS";

