alter table applicant
    add authenticate_code char(8) not null after id;

alter table applicant
    add authenticated boolean not null after authenticate_code;