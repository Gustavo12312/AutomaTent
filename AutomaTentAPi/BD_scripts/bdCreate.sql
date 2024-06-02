
create table usr(
    usr_id SERIAL not null,
    usr_name varchar(60) not null,
    usr_email varchar(200) not null,
    usr_pass varchar(200) not null, 
    usr_token VARCHAR(255),
    
    primary key (usr_id)
);

create table dev(
    dev_id SERIAL not null,
    dev_name varchar(60) not null,
    dev_value CHAR(1) NOT NULL CHECK (dev_value IN ('0', '1')),
    dev_value_string VARCHAR(200),
    
    primary key (dev_id)
);



//Android
Color Picker -> #FF00FF
Android (#FF00FF) -> Endpoint (BD -> "255,0,255")