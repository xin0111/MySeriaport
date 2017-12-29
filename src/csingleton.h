#ifndef CSINGLETON
#define CSINGLETON

#include <QAtomicPointer>
#include <QMutex>
#include <QReadWriteLock>

template <class T>
class Singleton
{
public:
    static T& getInstance(void);

private:
    Singleton();//forbid
    Singleton(const Singleton<T>&);
    Singleton<T>& operator=(const Singleton<T>&);

    QReadWriteLock  internalMutex;
    static QMutex mutex;
    static QAtomicPointer<T> m_pInstance;
};

template <class T>
QAtomicPointer<T>Singleton<T>::m_pInstance;

template <class T>
QMutex Singleton<T>::mutex;

template <class T>
T& Singleton<T>::getInstance(void)
{
    if(m_pInstance.testAndSetOrdered(0,0))
    {
        QMutexLocker locker(&mutex);

        m_pInstance.testAndSetOrdered(0,new T);
    }
    return *m_pInstance;
}

#endif // CSINGLETON

