TRUNCATE public.transfer_information RESTART IDENTITY;

TRUNCATE public.bank_account RESTART IDENTITY;

INSERT INTO public.bank_account (name_number, amount, person_id)
SELECT
        'Account ' || (row_number() OVER ())::text,
        CASE WHEN (row_number() OVER ()) <= 5 THEN 0 ELSE 5000 END,
        (SELECT id FROM person ORDER BY id LIMIT 1)
FROM generate_series(1, 10);
