# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "WAYS" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"line" geometry NOT NULL);

# --- !Downs

drop table "WAYS";

