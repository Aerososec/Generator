package dbStorage

// func setupTestDb() (*DbStorage, *gorm.DB) {
// 	db := NewDatabaseStorage().db
// 	tx := db.Begin()
// 	return &DbStorage{db: tx}, tx
// }
// func teardownTestDb(tx *gorm.DB) {
// 	tx.Rollback()
// }

// func TestGetExcuses(t *testing.T) {
// 	storage, tx := setupTestDb()
// 	defer teardownTestDb(tx)

// 	categories := []tables.Category{
// 		{ID: 404, CategoryName: "test1"},
// 	}
// 	for _, category := range categories {
// 		storage.db.Create(&category)
// 	}

// 	excuses := []tables.Excuse{
// 		{ID: 1339, Text: "I'm sick", CategoryID: 404, Plausibility: 1},
// 		{ID: 1340, Text: "I'm tired", CategoryID: 404, Plausibility: 2},
// 	}
// 	for _, excuse := range excuses {
// 		storage.db.Create(&excuse)
// 	}

// 	result, err := storage.GetExcusesByName("test1")

// 	assert.NoError(t, err)

// 	assert.Equal(t, excuses[0].Text, result[0].Text)

// 	assert.Equal(t, excuses[1].Text, result[1].Text)
// }
