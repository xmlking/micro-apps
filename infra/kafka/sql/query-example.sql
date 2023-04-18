
SELECT * FROM call_record
WHERE name = 'Louise' EMIT CHANGES;

SELECT * FROM location_of_interest
WHERE reason = 'allergy';
