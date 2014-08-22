# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "USERS" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"FULL_NAME" VARCHAR(254) NOT NULL,"WAY_ID" BIGINT NOT NULL);
create table "WAYS" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"line" geometry NOT NULL);
create table "WOODS" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"the_geom" geometry NOT NULL,"wood_id" VARCHAR(254),"tunnus" VARCHAR(254) NOT NULL,"kunta" VARCHAR(254) NOT NULL,"alue" VARCHAR(254) NOT NULL,"lohko" VARCHAR(254) NOT NULL,"mts" VARCHAR(254) NOT NULL,"kuvio" VARCHAR(254) NOT NULL,"alakuvio" VARCHAR(254),"pinta_ala" VARCHAR(254) NOT NULL,"ryhma" VARCHAR(254) NOT NULL,"luokka" VARCHAR(254) NOT NULL,"aika" VARCHAR(254),"teksti" VARCHAR(254),"owner" BIGINT NOT NULL,"ORIG_FILENAME" VARCHAR(254) NOT NULL);
alter table "USERS" add constraint "USER_WAY_FK" foreign key("WAY_ID") references "WAYS"("id") on update NO ACTION on delete NO ACTION;
alter table "WOODS" add constraint "PILE_OWNER_FK" foreign key("owner") references "USERS"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "WOODS" drop constraint "PILE_OWNER_FK";
alter table "USERS" drop constraint "USER_WAY_FK";
drop table "WOODS";
drop table "WAYS";
drop table "USERS";

