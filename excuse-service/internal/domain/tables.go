package domain

type Category struct {
	ID           int      `gorm:"primaryKey"`
	CategoryName string   `gorm:"column:category_name;not null"`
	Excuses      []Excuse `json:"-"`
}

type User struct {
	ID       string `gorm:"primaryKey"`
	UserName string `gorm:"column:username;not null"`
	Login    string `gorm:"column:login;not null;unique"`
	Password string `gorm:"column:password;not null"`
}

type Excuse struct {
	ID           int      `gorm:"primaryKey"`
	Text         string   `gorm:"column:text;not null"`
	CategoryID   int      `gorm:"column:category_id;not null"`
	Category     Category `gorm:"foreignKey:CategoryID"`
	Plausibility int      `gorm:"column:plausibility;not null"`
}

type UserFavorite struct {
	UserID   string `gorm:"primaryKey;foreignKey:UserID;references:ID"`
	ExcuseID int    `gorm:"primaryKey;foreignKey:ExcuseID;references:ID"`
	Excuse   Excuse `gorm:"foreignKey:ExcuseID"`
}

type Session struct {
	SessionID string `gorm:"primaryKey;column:session_id;not null"`
	UserID    string `gorm:"column:user_id;not null"`
	User      User   `gorm:"foreignKey:UserID;references:ID"`
}
