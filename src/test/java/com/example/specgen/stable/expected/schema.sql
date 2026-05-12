
CREATE TABLE users (
    id BIGINT
     PRIMARY KEY
    
    
    ,
    email TEXT
    
     UNIQUE
    
    ,
    passwordHash TEXT
    
    
    
    ,
    age INTEGER
    
    
     NOT NULL
    
);