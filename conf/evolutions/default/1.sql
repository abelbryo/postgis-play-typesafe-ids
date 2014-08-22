# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "USERS" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"FULL_NAME" VARCHAR(254) NOT NULL,"WAY_ID" BIGINT NOT NULL);
create table "WAYS" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"line" geometry NOT NULL);
create table "WOODS" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"wkb_geometry" geometry NOT NULL,"wood_id" BIGINT,"tunnus" VARCHAR(254) NOT NULL,"kunta" BIGINT NOT NULL,"alue" BIGINT NOT NULL,"lohko" BIGINT NOT NULL,"mts" DOUBLE PRECISION NOT NULL,"kuvio" DOUBLE PRECISION NOT NULL,"alakuvio" VARCHAR(254),"pinta_ala" DOUBLE PRECISION NOT NULL,"ryhma" DOUBLE PRECISION NOT NULL,"luokka" DOUBLE PRECISION NOT NULL,"aika" VARCHAR(254),"teksti" VARCHAR(254),"owner" BIGINT NOT NULL,"ORIG_FILENAME" VARCHAR(254) NOT NULL);
alter table "USERS" add constraint "USER_WAY_FK" foreign key("WAY_ID") references "WAYS"("id") on update NO ACTION on delete NO ACTION;
alter table "WOODS" add constraint "PILE_OWNER_FK" foreign key("owner") references "USERS"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "WOODS" drop constraint "PILE_OWNER_FK";
alter table "USERS" drop constraint "USER_WAY_FK";
drop table "WOODS";
drop table "WAYS";
drop table "USERS";

