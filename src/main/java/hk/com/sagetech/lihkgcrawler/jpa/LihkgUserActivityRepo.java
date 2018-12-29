package hk.com.sagetech.lihkgcrawler.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LihkgUserActivityRepo extends CrudRepository<UserActivityModel, UserActivityCompositeKey> {
}
