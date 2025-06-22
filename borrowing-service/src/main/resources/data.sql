-- Initial borrowing records
INSERT INTO borrowings (book_id, user_id, borrow_date, return_date, returned) 
VALUES 
    (1, 1, '2024-01-15', '2024-02-15', true),
    (2, 1, '2024-02-01', '2024-03-01', false),
    (3, 2, '2024-02-10', '2024-03-10', false);
