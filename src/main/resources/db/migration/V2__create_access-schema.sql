CREATE table access_log(id SERIAL primary key, request_at timestamp, response_at timestamp, session_id varchar(50), controller_name varchar(150), method_name varchar(50), arguments text, http_method varchar(10), request_uri text, response_status varchar(10), around_microsecond integer);
