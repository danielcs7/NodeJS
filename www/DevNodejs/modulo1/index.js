const express = require('express')
const nunjucks = require('nunjucks')
const app = express()

nunjucks.configure('views', {
  autoescape: true,
  express: app,
  watch: true
})

app.use(express.urlencoded({ extended: false }))

app.set('view engine', 'njk')

// aqui crio array
const users = ['Samira Santos', 'Sofia Santos', 'Daniel Santana']

app.get('/', (req, res) => {
  return res.render('list', { users })
})

app.get('/new', (req, res) => {
  return res.render('new')
})

app.post('/create', (req, res) => {
  // push para adicionar dados no array
  users.push(req.body.user)

  // redirecionar para a listagem
  return res.redirect('/')
})

app.listen(3000)
