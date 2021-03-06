create table snapshot (
	id bigint generated by default as identity not null,
	date_ date not null,
	communes varchar(256) not null
);
alter table snapshot add constraint snapshot_pk primary key (id);

create table annonce (
	id bigint generated by default as identity not null,
	prix integer not null,
	surface integer not null,
	snapshot_id bigint
);
alter table annonce add constraint annonce_pk primary key (id);
--alter table annonce add constraint snapshot_id_fk foreign key (snapshot_id) references snapshot (id);
