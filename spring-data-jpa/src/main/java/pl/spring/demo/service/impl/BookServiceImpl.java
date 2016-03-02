package pl.spring.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.spring.demo.entity.BookEntity;
import pl.spring.demo.mapper.BookMapper;
import pl.spring.demo.repository.BookRepository;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Override
	public List<BookTo> findAllBooks() {
		return BookMapper.map2To(bookRepository.findAll());
	}

	@Override
	public List<BookTo> findBooksByTitle(String title) {
		return BookMapper.map2To(bookRepository.findBookByTitle(title));
	}

	@Override
	public List<BookTo> findBooksByAuthor(String author) {
		return BookMapper.map2To(bookRepository.findBookByAuthor(author));
	}

	@Override
	public List<BookTo> findBooks(String title, String authors) {
		List<BookEntity> findBook = null;
		if (!title.isEmpty() && !authors.isEmpty()) {
			findBook = bookRepository.findBookByTitleAndByAuthors(title, authors);
		} else if (!title.isEmpty()) {
			findBook = bookRepository.findBookByTitle(title);
		} else if (!authors.isEmpty()) {
			findBook = bookRepository.findBookByAuthor(authors);
		}
		return BookMapper.map2To(findBook);
	}

	@Override
	@Transactional(readOnly = false)
	public BookTo saveBook(BookTo book) {
		BookEntity entity = BookMapper.map(book);
		entity = bookRepository.save(entity);
		return BookMapper.map(entity);
	}

	@Override
	@Transactional
	public BookTo deleteBook(Long id) {
		BookEntity book = null;
		if (id != null && bookRepository.exists(id)) {
			book = bookRepository.getOne(id);
			bookRepository.delete(book);
		}
		return BookMapper.map(book);
	}

	@Override
	@Transactional
	public BookTo deleteBook(BookTo bookTo) {
		BookEntity bookEntity = BookMapper.map(bookTo);
		Long id = bookEntity.getId();
		if (id != null && bookRepository.exists(id) && bookRepository.getOne(id).equals(bookEntity)) {

			bookRepository.delete(bookEntity);
			return bookTo;

		}
		return null;
	}

	@Override
	@Transactional
	public BookTo updateBook(BookTo bookTo) {
		Long id = bookTo.getId();
		BookEntity bookEntity = null;
		if (id != null && bookRepository.exists(id)) {
			bookEntity = bookRepository.findOne(id);
			bookEntity.setAuthors(bookTo.getAuthors());
			bookEntity.setTitle(bookTo.getTitle());
			bookEntity = bookRepository.getOne(id);
		}
		return bookEntity == null ? bookTo : BookMapper.map(bookEntity);
	}

}
