CREATE TABLE PAY_SPRINKLE
(

 	token_id	CHAR	(3)	not null
,	room_id		VARCHAR	(255)	not null
, 	pay_snd_id	VARCHAR	(255)	not null
, 	pay_snd_amt	INT		not null
, 	pay_snd_count	INT		not null
, 	pay_snd_date	TIMESTAMP	not null
, 	CONSTRAINT PK_PAY_SPRINKLE PRIMARY KEY (token_id)
);


CREATE TABLE PAY_SPRINKLE_HIST
(
 	token_id	CHAR	(3)	not null
, 	seq		INT		not null
, 	room_id		VARCHAR	(255)	not null
, 	pay_rcv_id	VARCHAR	(255)	
, 	pay_rcv_amt	INT		not null
, 	pay_rcv_yn	CHAR	(1)	
, 	pay_rcv_date	TIMESTAMP
, 	CONSTRAINT PK_PAY_SPRINKLE_HIST PRIMARY KEY (token_id, seq)
);
